var screenwidth = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
var screenheight = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
var scrollDiff;
var artnavOpacity = 0;
var artnavTop;
var timeOut, topOfPage;

function scrollInit() {
	var scrolling = document.getElementById("scrolling");
	scrolling.style.top = screenheight + "px";

	scrollDiff = document.getElementsByTagName("header")[0].clientHeight - screenheight;
	if (scrollDiff < 300) {
		scrollDiff = 300; // ok if a little bg shows
	}

	stickyScroll();

	var mq = window.matchMedia("(max-device-width:769px)");
	if(mq.matches) {
		topOfPage = 0;
	} else {
		topOfPage = document.getElementById("scrolling").offsetTop;
	}
};

function stickyScroll() {
	if (document.body.scrollTop > scrollDiff || document.documentElement.scrollTop > scrollDiff) {
		var head = document.getElementsByTagName("header")[0];
		head.className = "stuck";
		var top = (scrollDiff - 40) * -1;
		head.style.top = top + "px";
	} else {
		var head = document.getElementsByTagName("header")[0];
		head.className = "unstuck";
		head.style.top = "40px";
	}
};

function artnavInit() {
	artnavTop = document.getElementById("artnav").getBoundingClientRect().top + 200;

	var scrollingPos = document.getElementById("scrolling").getBoundingClientRect().top;
	if (scrollingPos <= artnavTop) {
		var artnav = document.getElementById("artnav");
		artnavOpacity = 1;
		artnav.style.opacity = artnavOpacity;
		artnav.style.filter = "alpha(opacity=100)";
	}
}

function artnavScroll() {
	var artnav = document.getElementById("artnav");
	var scrollingPos = document.getElementById("scrolling").getBoundingClientRect().top;
	if (scrollingPos <= artnavTop && artnavOpacity < 1) {
		artnavOpacity += 0.25;
		artnav.style.opacity = artnavOpacity;
		var lrgOpacity = artnavOpacity * 10;
		artnav.style.filter = "alpha(opacity="+lrgOpacity+")";
	} else if (scrollingPos > artnavTop && artnavOpacity > 0) {
		artnavOpacity -= 0.25;
		artnav.style.opacity = artnavOpacity;
		var lrgOpacity = artnavOpacity * 10;
		artnav.style.filter = "alpha(opacity="+lrgOpacity+")";
	}
}

function scrollToTop() {
	if (document.body.scrollTop > topOfPage || document.documentElement.scrollTop > topOfPage){
		window.scrollBy(0,-50);
		timeOut=setTimeout('scrollToTop()',10);
	} else {
		clearTimeout(timeOut);
	}
}