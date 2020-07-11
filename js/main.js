window.onload = function() {
	scrollInit();

    if(
           navigator.userAgent.match(/Android/i)
        || navigator.userAgent.match(/webOS/i)
        || navigator.userAgent.match(/iPhone/i)
        || navigator.userAgent.match(/iPad/i)
        || navigator.userAgent.match(/iPod/i)
        || navigator.userAgent.match(/BlackBerry/i)
        || navigator.userAgent.match(/Windows Phone/i)
    ) {
        mobileMenuInit();
    }

	if(document.getElementById("artnav") != null) {
		artnavInit();
	}

	if(document.getElementsByClassName("divide").length > 0) {
		extendCenter();
	}

	if(document.getElementById("changeImages") != null) {
		changeImages();
	}
}

window.onresize = function() {
	if(document.getElementsByClassName("divide").length > 0) {
		extendCenter();
	}
};

window.onscroll = function() {
	stickyScroll();

	if(document.getElementById("artnav") != null) {
		artnavScroll();
	}
};
