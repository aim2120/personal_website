function extendCenter() {
	var center = document.getElementById("divide");
	var lH = document.getElementById("left").clientHeight;
	var rH = document.getElementById("right").clientHeight;
	if (lH > rH) {
		console.log("using left " + lH);
		center.style.height = lH+"px";
	} else if (rH > lH) {
		console.log("using right " + rH);
		center.style.height = rH+"px";
	}
}

window.onload = function() {extendCenter()};
window.onresize = function() {extendCenter()};