function stickyScroll() {
    if (document.body.scrollTop > 340 || document.documentElement.scrollTop > 340) {
        document.getElementsByTagName("header")[0].className = "stuck";
    } else {
        document.getElementsByTagName("header")[0].className = "unstuck";
    }
}

window.onscroll = function() {stickyScroll()};