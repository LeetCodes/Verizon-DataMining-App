<?php

require('config.php');

$result = mysql_query("SELECT * FROM data");

$markers_array = array( 'markers' => array() ); 


while($row = mysql_fetch_array($result))
{
	if(strcmp($row['AREA'], "UNKNOWN") != 0){
		array_push($markers_array['markers'], array(
			'lat' => $row['LATITUDE'], 
			'lng' => $row['LONGITUDE'], 
			'title' => $row['EMAIL'],
			'msg' => $row['AREA']
		));
	}
}

$json = json_encode($markers_array);

echo $json;
 
?>
