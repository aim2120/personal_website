var timeOut, topOfPage;
var mq = window.matchMedia("(max-device-width:769px)");
if(mq.matches) {
	topOfPage = 0;
} else {
	topOfPage = document.getElementById("scrolling").offsetTop;
}

function scrollToTop() {
	if (document.body.scrollTop > topOfPage || document.documentElement.scrollTop > topOfPage){
		window.scrollBy(0,-50);
		timeOut=setTimeout('scrollToTop()',10);
	} else {
		clearTimeout(timeOut);
	}
}
