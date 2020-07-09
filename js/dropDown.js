var pastWidth = 0, maxWidth, width, timeOut;
function dropDown(li) {
	var top = li.children[0];
	var drop = li.children[1];
	maxWidth = top.clientWidth * drop.children.length;
	width = drop.clientWidth;
	if(pastWidth < width || pastWidth == 0) { // sliding out
		if(width < maxWidth) {
			drop.style.width = drop.clientWidth + 5 + "px";
			pastWidth = width;
			timeOut=setTimeout(function() {dropDown(li)},5);
		} else {
			pastWidth = width;
			clearTimeout(timeOut);
		}
	} else { // sliding in
		if(width > 0) {
			drop.style.width = drop.clientWidth - 5 + "px";
			pastWidth = width;
			timeOut=setTimeout(function() {dropDown(li)},5);
		} else {
			pastWidth = width;
			clearTimeout(timeOut);
		}
	}
}