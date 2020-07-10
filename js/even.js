function extendCenter() {
	var mq = window.matchMedia("(max-device-width:769px)");
	if(!mq.matches) {
		// must have 1:1:1 ratio of left, divide, and right DIVs
		var centerDIVs = document.getElementsByClassName("divide");
		var leftDIVs = document.getElementsByClassName("left");
		var rightDIVs = document.getElementsByClassName("right");
		for(var i = 0; i < leftDIVs.length; i++) {
			var center = centerDIVs[i];
			var lH = leftDIVs[i].clientHeight;
			var rH = rightDIVs[i].clientHeight;
			if (lH > rH) {
				center.style.height = lH+"px";
			} else if (rH > lH) {
				center.style.height = rH+"px";
			}
		}
	}
}
