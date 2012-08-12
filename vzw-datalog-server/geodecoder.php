<?php
	//Gets  Area useing Google's geodecoder
	function geodecode($latitude, $longitude){
		$area = "UNKNOWN";
		
		if ( !empty($latitude) && !empty($longitude)){
			$geodecode_url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" . $latitude . "," . $longitude . "&sensor=false";
			$result = file_get_contents($geodecode_url,0,null,null);
			$geodecode_json = json_decode($result, true);
			
			if ( strcmp($geodecode_json['status'],"OK") == 0){
				$area = $geodecode_json['results'][1]['formatted_address'];
			}
		}
		
		return $area;
	}
?>