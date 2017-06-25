/**
 * 
 */
$(window).on('load', function () {
	resizeToFit();
});

/**
 * Resize base components to fit the window without scrolling header and footer
 */
function resizeToFit() {
	//Auto-set max-height attribute of left side bar and main area based on header and footer height
	var pageHeight = $('body').height();
	var headerHeight = $('body>header').height();
	var footerHeight = $('body>footer').height();
	var contentMaxHeight = pageHeight - (headerHeight + footerHeight);
	$('body>main>section:first-child').height(contentMaxHeight);
	$('body>main>section:first-child>article').css('max-height', contentMaxHeight);
}