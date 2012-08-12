<?php
	function sendMail($json, $area){
		$email = $json['EMAIL'];
		if(strlen($email) > 0){
			$subject = "VZW-Datalog Server Entry Confimation";
			$header = "From: no-reply@vzwserver.com";
			
			$msg = "Sucessful data entry:\n";
			$msg .= "\nTimestamp: ".date("d-m-y G:i:s");
			$msg .= "\n Id: ".$json['IDENTIFIER'];
			$msg .= "\n Phone Number: ".$json['PHONE_NUMBER'];
			$msg .= "\n Area: ".$area;
			$msg .= "\n Signal Strenght: ".$json['SIGNAL_STRENGTH'];
			$msg .= "\n Signal Type: ".$json['SIGNAL_TYPE'];
			$msg .= "\n Phone Model: ".$json['MODEL'];
			$msg .= "\n Android Version: ".$json['ANDROID_VER'];
			$msg .= "\n Kernel Version: ".$json['KERNEL_VER'];
			$msg .= "\n Free Memory: ".$json['MEMORY'];
			$msg .= "\n Amount of Storage: ".$json['SD_STORAGE'];
			$msg .= "\n Amount of Dropped Calls: " .$json['DROP_CALLS'];
			
			mail($email, $subject, $msg, $header);
		}
	}
?>