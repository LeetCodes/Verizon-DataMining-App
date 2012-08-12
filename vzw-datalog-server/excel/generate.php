<? 
require_once('PHPExcel/MySqlExcelBuilder.class.php');
require('../config.php');
// Intialize the object with the database variables

$mysql_xls = new MySqlExcelBuilder($database,$user,$pwd,$host);

// Setup the SQL Statements
$sql_statement = <<<END_OF_SQL

SELECT `TIME` AS `Time`, `IDENTIFIER` AS `IMEI`, `AREA` AS `Area`, `SIGNAL_STRENGTH` AS `Signal Strength`, `SIGNAL_TYPE` AS `Signal Type`, `ANDROID_VER` AS `Android Version`, `KERNEL_VER` AS `Kernel`, `MEMORY` AS `Memory`, `SD_STORAGE` AS `SD Storage`, `DROP_CALLS` AS `Drop Calls`, `EMAIL` AS `Email`, `MESSAGE` AS `Message` FROM `data` 

END_OF_SQL;

// Add the SQL statements to the spread sheet
$mysql_xls->add_page('Data',$sql_statement);

// Get the spreadsheet after the SQL statements are built...
$phpExcel = $mysql_xls->getExcel(); // This needs to come after all the pages have been added.

// Write the spreadsheet file...cv 
$objWriter = PHPExcel_IOFactory::createWriter($phpExcel, 'Excel5'); // 'Excel5' is the oldest format and can be read by old programs.
$fname = "VZW-DB.xls";
$objWriter->save($fname);

// Make it available for download.
//echo "<a href=\"$fname\">Download $fname</a>";


?>
