function see(elem) {
	var span = elem.children[1];
	span.className = "visible";
	var img = elem.children[0];
	img.style.opacity = "0.6";
	img.style.filter = "alpha(opacity=60)";
}

function hide(elem) {
	var span = elem.children[1];
	span.className = "hidden";
	var img = elem.children[0];
	img.style.opacity = "1.0";
	img.style.filter = "alpha(opacity=100)"
}