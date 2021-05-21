function toggleMenu(){
    const menu = document.getElementById("menu");
    const cover = document.getElementById("menu-cover");
    if(!menu.offsetLeft) {
        menu.style.left = "-300px";
        cover.style.display = "none";
    }
    else {
        menu.style.left = "0px";
        cover.style.display = "block";
    }
    return false;
}
