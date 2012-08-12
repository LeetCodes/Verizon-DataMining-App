<!DOCTYPE html>
<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> 
<html lang="en"> <!--<![endif]-->
<head>
    <meta charset="utf-8" />
    <!-- Set the viewport width to device width for mobile -->
    <meta name="viewport" content="width=device-width" />

    <title>VZW-DataLog</title>

    <!-- Included CSS Files -->
    <link rel="stylesheet" href="stylesheets/foundation.css">
    <link rel="stylesheet" href="stylesheets/app.css">

    <!--[if lt IE 9]>
    <link rel="stylesheet" href="stylesheets/ie.css">
    <![endif]-->


    <!-- IE Fix for HTML5 Tags -->
    <!--[if lt IE 9]>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <!-- Included Javascript Files -->
    <script src="javascripts/foundation.js"></script>
    <script src="javascripts/jquery.columnfilters.js"></script>
    <script src="javascripts/app.js"></script>
    <script src="http://maps.google.com/maps/api/js?sensor=false"></script>
    <script type="text/javascript" src="javascripts/jquery.gomap-1.3.2.min.js"></script> 

</head>
<body>
<!-- HEADER -->
    <div id="zurBar" class="container">
        <div class="row">
            <div class="four columns">
            <h1><a href="index.php">VZW - DataLog App</a></h1>
            </div>
        <div class="eight columns hide-on-phones">
            <strong class="right">
            <a href="/phpmyadmin">phpMyAdmin</a>
            <a href="data.php">View Data</a>
            <a href="map.php">View Map</a>
            <a href="#" onclick="generateExcel();">Download Excel</a>
            <a id="downloadButton" href="vzw-datalog.apk" class="small red nice button src-download">Download</a>
            </strong>
        </div>
        </div>
    </div>
<!-- /HEADER -->

<!-- container -->
<div class="container">
