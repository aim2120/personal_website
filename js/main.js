window.onload = function() {
	scrollInit();

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