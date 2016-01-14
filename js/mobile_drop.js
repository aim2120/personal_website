function dropDown(li) {
	var mq = window.matchMedia("(max-device-width:769px)");
	if(mq.matches) {
		var drop = li.children[1];
		if(drop.style.display == "none") {
			drop.style.display = "inline-block";
		} else {
			drop.style.display = "none";
		}
	}
}