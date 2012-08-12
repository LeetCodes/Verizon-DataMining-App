/* Foundation v2.1.1 http://foundation.zurb.com */
$(document).ready(function() {



           
    /* Use this js doc for all application specific JS */

    /* TABS --------------------------------- */
    /* Remove if you don't need :) */

    var tabs = $('dl.tabs');
    tabsContent = $('ul.tabs-content')

        tabs.each(function(i) {
            //Get all tabs
            var tab = $(this).children('dd').children('a');
            tab.click(function(e) {

                //Get Location of tab's content
                var contentLocation = $(this).attr("href")
                contentLocation = contentLocation + "Tab";

            //Let go if not a hashed one
            if(contentLocation.charAt(0)=="#") {

                e.preventDefault();

                //Make Tab Active
                tab.removeClass('active');
                $(this).addClass('active');

                //Show Tab Content
                $(contentLocation).parent('.tabs-content').children('li').css({"display":"none"});
                $(contentLocation).css({"display":"block"});

            } 
            });
        });


    /* PLACEHOLDER FOR FORMS ------------- */
    /* Remove this and jquery.placeholder.min.js if you don't need :) */

    $('input, textarea').placeholder();


    /* DISABLED BUTTONS ------------- */
    /* Gives elements with a class of 'disabled' a return: false; */

    /* FILTER TABLES ------------- */
    /* Jquery plugin for filtering tables dynamically */

	$(document).ready(function() {
		$('table#filterTable1').columnFilters({alternateRowClassNames:['rowa','rowb']});
		/*$('table#filterTable2').columnFilters({alternateRowClassNames:['rowa','rowb'], excludeColumns:[2,3]});
		$('table#filterTable3').columnFilters({caseSensitive:true});
		$('table#filterTable4').columnFilters({minSearchCharacters:3});
		$('table#filterTable5').columnFilters({wildCard:'#',notCharacter:'?'}); */
	});


    /* MAP MAGIC ------------- */
    
    $("#map").goMap({ 
        scaleControl: true, 
        maptype: 'ROADMAP', 
        latitude: 18.15, 
        longitude: -66.30, 
        zoom: 9,
    }); 

    $.get('map_json.php', function(data) {

	for(var i = 0, l = data.markers.length; i < l; i++) {
        var marker = data.markers[i];
		$.goMap.createMarker({
			latitude: marker.lat,
			longitude: marker.lng,
            title: marker.title,
            html: {content: marker.msg }
			});
			}
		}, 'json');
    
    /* SLIDESHOW MAGIC ------------- */

    $(window).load(function() {
       $('#featured').orbit({
           directionalNav: false, 
           pauseOnHover: true,
           startClockOnMouseOut: true,
           startClockOnMouseOutAfter: 500
           });
    });

    /* Active Link MAGIC ------------- */

    var url = window.location.pathname, 
        urlRegExp = new RegExp(url.replace(/\/$/,'') + "$"); 
        // create regexp to match current url pathname and remove trailing slash 
        // if present as it could collide with the link in navigation in case trailing 
        // slash wasn't present there
        // now grab every link from the navigation
        if(url != "/"){
            $('dd > a').each(function(){
             // and test its normalized href against the url pathname regexp
             if(urlRegExp.test(this.href.replace(/\/$/,''))){
                 $(this).addClass('active');
             }
            });
        }else{
            $('dd > a').first().addClass('active');
        }

});

/* EXCEL MAGIC ------------- */
    function generateExcel(){
        $.get("excel/generate.php");
        setTimeout(function() {  window.location.href = 'excel/VZW-DB.xls'; } , 1000);
    }



   
