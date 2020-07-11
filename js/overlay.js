function toggleTitle(elem) {
	var span = elem.children[1];
	if(span.className == "visible") {
		span.className = "hidden";
		var img = elem.children[0];
		img.style.opacity = "100%";
	} else {
		span.className = "visible";
		var img = elem.children[0];
		img.style.opacity = "80%";
	}
}
