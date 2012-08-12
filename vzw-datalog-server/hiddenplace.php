<!DOCTYPE html>

<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en"> <!--<![endif]-->
<head>
	<meta charset="utf-8" />

	<!-- Set the viewport width to device width for mobile -->
	<meta name="viewport" content="width=device-width" />

	<title>Hidden Place</title>
  
	<!-- Included CSS Files -->
	<link rel="stylesheet" href="/stylesheets/foundation.css">
	<link rel="stylesheet" href="/stylesheets/app.css">

	<!--[if lt IE 9]>
		<link rel="stylesheet" href="stylesheets/ie.css">
	<![endif]-->


	<!-- IE Fix for HTML5 Tags -->
	<!--[if lt IE 9]>
		<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->

</head>
<body>

	<!-- container -->
	<div class="container">

		<div class="row">
			<div class="twelve columns">
				<h2>Welcome to a hidden place! o.O</h2>
			</div>
		</div>

		<div class="row">
			<div class="twelve columns">
        	  	<p>Enter your name:</p>
                <input id="name" class="input-text" type="text"> 
                <a class="blue button radius" onClick="greet()">Say Something Nice!</a> 
                <br />
                <br />
                <p id="output"></p>
                <script type="text/javascript">
					function greet(){
						var name = document.getElementById("name").value;
						if(name.length > 0){
							document.getElementById("output").innerHTML="<h1>Love and be loved, " + name + ".</h1>";
						} else {
							document.getElementById("output").innerHTML="";
						}
					}
				</script>
			</div>
		</div>

	</div>
	<!-- container -->
    
	<!-- Included JS Files -->
	<script src="../javascripts/jquery.min.js"></script>
	<script src="../javascripts/modernizr.foundation.js"></script>
	<script src="../javascripts/foundation.js"></script>
	<script src="../foundation-download-2.1.5/javascripts/app.js"></script>

</body>
</html>
