function mobileMenuInit() {
    var button = document.getElementById("navbutton");
    button.style.display = "block";
    var menu = document.getElementsByTagName("nav")[0];
    menu.style.display = "none";
    menu.style.position = "fixed";
};

function mobileMenuToggle() {
    var button = document.getElementById("navbutton");
    var menu = document.getElementsByTagName("nav")[0];
    if (menu.style.display == "none") {
        menu.style.display = "block";
        button.className = "inverse";
    } else {
        menu.style.display = "none";
        button.className = "";
    }
}
