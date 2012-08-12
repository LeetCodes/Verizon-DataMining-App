<? 
    // You may need to be certain your include paths are all setup properly.
    require_once('PHPExcel.php');
    
    /**
     * MySqlExcelBuilder
     * 
     * @package MySqlBuilder Tutorial
     * @author David Brumbaugh
     * @copyright 2011
     * @license LGPL Version 3
     * @access public
     */
    class MySqlExcelBuilder
    {
        protected $pdo; // PHP Data Object
        public $phpExcel; // PHP Excel 
        protected $sql_pages = array(); //Sheet Name, Sql Statement, Options
        protected $column_map = array();    
                  
        /**
         * MySqlExcelBuilder::__construct()
         * 
         * @param mixed $db  - Database
         * @param mixed $un  - User name
         * @param mixed $pw  - Password
         * 
         */
        public function __construct($db,$un,$pw,$host)
        {            
            //$host = 'localhost';
            $this->phpExcel = new PHPExcel();
            $dsn =  "mysql:host=$host;port=8889;dbname=$db";             
            try 
            {
                $this->pdo = new PDO($dsn, $un, $pw); 
                $this->pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_WARNING);             
            } 
            catch (PDOException $e) 
            {
                $this->$pdo  = null;                
                error_log("$dsn\n".'Connection failed: ' . $e->getMessage());                                                          
            }
        }  
        
        /**
         * MySqlExcelBuilder::add_page()
         * 
         * Adds a page to the MySqlExcelBuilder to be rendered later.
         * 
         * @param mixed $wsName - Name of the Worksheet
         * @param mixed $sql - SQL Statement to store in the worksheet
         * @param mixed $total_colums - Comma Delimited List of Columns to Total (Column Names, NOT Labels)
         * @param string $start_col  - Starting Column (Must be between "A" and "Z")
         * @param string $start_row  - Row number to start on. Starting with 1. That will be where the column labels go.
         * @return void
         */
        public function add_page($wsName,$sql,$total_colums=null,$start_col="A",$start_row="1")
        {
          
            $this->sql_pages[$wsName]['Sql'] = trim($sql);
            $this->sql_pages[$wsName]['Col'] = $start_col;
            $this->sql_pages[$wsName]['Row'] = $start_row;
            $this->sql_pages[$wsName]['Totals'] = $total_colums;
            
        }        
        
        /**
         * MySqlExcelBuilder::mapColumns()
         * 
         * Maps the named columns in a particular query to the alphabetical columns in a spreadsheet.
         * 
         * @param mixed $wsSheet - Worksheet name
         * @param mixed $keys - Array of column names
         * @param mixed $start_col - Column to start with (Must be between "A" and "Z")
         * @return void
         */
        protected function mapColumns($wsSheet,$keys,$start_col)
        {
            $min = ord($start_col); // ord returns the ASCII value of the first character of string.            
            $firstChar = ""; // Initailize the First Character
            $abc = $min;   // Intialize our alphabetical counter
            foreach($keys as $key)
            {
            	$col = $firstChar.chr($abc);   // This is the Column Label. 
                $last_char = substr($col, -1);
            	if ($last_char> "Z") // At the end of the alphabet. Time to Increment the first column letter.
            	{	 
            		$abc = $min; // Start Over
            		if ($firstChar == "") // Deal with the first time.
            			$firstChar = "A";
            		else 
            		{
            			$fchrOrd = ord($firstChar); // Get the value of the first character
            			$fchrOrd++; // Move to the next one.
            			$firstChar = chr($fchrOrd); // Reset the first character.
            		}		
            		$col = $firstChar.chr($abc); // This is the column identifier
            	}
            	
               // This three dimensional array is a two way map, allowing you to 
               //  match database column names to spreadsheet labels               
               $this->column_map[$wsSheet]['xls'][$key] = $col;
               $this->column_map[$wsSheet]['sql'][$col] = $key;
            
              $abc++; // Move on to the next letter
            }
          
        }
        
        /**
         * MySqlExcelBuilder::getExcel()
         * 
         * Builds the spreadhseet pages from the SQL statements added in add_page.
         * 
         * @return PHPExcel object
         */
        public function getExcel()
        {                       
            $i = 0;            
            foreach($this->sql_pages as $wsName=>$page)
            {                             
                $start_of_page = true;
                $sql = $page['Sql'];              
                $start_col = $page['Col'];
                $start_row = $page['Row'];
                $this->phpExcel->createSheet();
                $sheet = $this->phpExcel->setActiveSheetIndex($i);
                                    
                if ($sh = $this->pdo->query($sql))
                {        
                    $rowNum = $start_row;                                        
                    while($row = $sh->fetch(PDO::FETCH_ASSOC))
                    {
                        $keys = array_keys($row); // Get the Column Names
                        if ($start_of_page) // Initialize the Page
                        {
                            $this->mapColumns($wsName,$keys,$start_col);
                            foreach($keys as $key)
                            {
                               $col = $this->column_map[$wsName]['xls'][$key];
                               $cellKey = $col.$rowNum; 
                                $sheet->setCellValue($cellKey,$key);
                                $style = $sheet->getStyle($cellKey);
                                // The next sevral lines are for formatting your header
                                // If you want to make the header gray, uncomment out the lines below.
                                /*
                                $style->getFill()->setFillType(PHPExcel_Style_Fill::FILL_SOLID);
                                $style->getFill()->getStartColor()->setARGB('FFCACACA');                                
                                */
                                $style->getFont()->setBold(true);
                                $sheet->getColumnDimension($col)->setAutoSize(true);
                            }
                            $rowNum++; // The next row is for data
                            $start_of_page = false; // Done with Intialization   
                        }
                        foreach($keys as $key) // Put the value of the data into each cell
                        {
                             $col = $this->column_map[$wsName]['xls'][$key]; // Get the appropriate column
                             $cellKey = $col.$rowNum; // Build the column key
                             $val = $row[$key]; // Get the data value
                             $sheet->setCellValue($cellKey,$val); // Put it in the cell.
                        }                                                  
                        $rowNum++;                        
                    }
                    $this->sql_pages[$wsName]['lastDataRow'] = $rowNum; 
                      
                    if ($this->sql_pages[$wsName]['Totals'])
                    {
                        $rowNum++;
                        $total_columns = explode(',',$this->sql_pages[$wsName]['Totals']);
                        foreach($total_columns as $key)
                        {
                         
                            $col = $this->column_map[$wsName]['xls'][$key];
                            // Add the Total Label
                            $cellLabelKey = $col.$rowNum; 
                            $total_label = "Total $key";
                            $sheet->setCellValue($cellLabelKey,$total_label); 
                            $style = $sheet->getStyle($cellLabelKey);                              
                            $style->getFont()->setBold(true);
                            
                            // Add the actual totals
                            $total_row = $rowNum+1;
                            $cellKey = $col.$total_row;                           
                            $startTotal = $col.$start_row;
                            $endTotal = $col.$this->sql_pages[$wsName]['lastDataRow']; 
                            $total_forumla = "=SUM($startTotal:$endTotal)";                            
                            $sheet->setCellValue($cellKey,$total_forumla);
                            $style = $sheet->getStyle($cellKey);                              
                            $style->getFont()->setBold(true);
                           
                                                       
                        }
                        
                    } 
                                    
                    $this->phpExcel->getActiveSheet()->setTitle($wsName);                   
                }
                else
                {
                    error_log("Query Failed: $sql");
                }
                
                ++$i;
                
            }
            return $this->phpExcel;
        }
                
        
    }
    

?>
