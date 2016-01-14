var timeOut;
var main = document.getElementById("scrolling").getBoundingClientRect();
function scrollToTop() {
	if (document.body.scrollTop >= main.top || document.documentElement.scrollTop >= main.top){
		window.scrollBy(0,-50);
		timeOut=setTimeout('scrollToTop()',5);
	} else {
		clearTimeout(timeOut);
	}
}