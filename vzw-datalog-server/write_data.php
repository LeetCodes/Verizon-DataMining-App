<?php

//Include geodecoder
include('geodecoder.php');

//Include E-mail function
include('send_mail.php');

//Requires MySQL config
require('config.php');

//Get data
$data = strip_tags(file_get_contents('php://input'));  
//Decode JSON string
$json = json_decode($data, true);  


$IDENTIFIER = $json['IDENTIFIER']; 
$PHONE_NUMBER = $json['PHONE_NUMBER'];  

$LONGITUDE = $json['LONGITUDE'];  
$LATITUDE = $json['LATITUDE'];
$ALTITUDE = $json['ALTITUDE'];
$AREA = geodecode($LATITUDE, $LONGITUDE);
  
$SIGNAL_STRENGTH = $json['SIGNAL_STRENGTH'];  
$SIGNAL_TYPE = $json['SIGNAL_TYPE'];
  
$MODEL = $json['MODEL'];
$ANDROID_VER = $json['ANDROID_VER'];  
$KERNEL_VER = $json['KERNEL_VER'];  

$EMAIL = $json['EMAIL'];  
$MESSAGE = $json['MESSAGE']; 
 
$MEMORY = $json['MEMORY']; 
$SD_STORAGE = $json['SD_STORAGE'];
  
$DROP_CALLS = $json['DROP_CALLS'];

//Send mail
sendMail($json, $AREA);   

//Insert data into database
    mysql_query("INSERT INTO data (IDENTIFIER, PHONE_NUMBER, LONGITUDE, LATITUDE, ALTITUDE, AREA, SIGNAL_STRENGTH, SIGNAL_TYPE, MODEL, ANDROID_VER, KERNEL_VER, EMAIL, MESSAGE, MEMORY, SD_STORAGE, DROP_CALLS) VALUES ('$IDENTIFIER', '$PHONE_NUMBER', '$LONGITUDE',  '$LATITUDE', '$ALTITUDE', '$AREA', '$SIGNAL_STRENGTH', '$SIGNAL_TYPE', '$MODEL', '$ANDROID_VER', '$KERNEL_VER', '$EMAIL', '$MESSAGE', '$MEMORY', '$SD_STORAGE', '$DROP_CALLS' )");


mysql_close($con);
?>
