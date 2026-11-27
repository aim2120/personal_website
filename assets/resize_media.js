// make img elements the correct size dynamically so they hold the correct screen space while imgs loading
function resizeMedia() {
    const mq = window.matchMedia("(max-device-width: 812px)");
    const percent_width = mq.matches ? 1.0 : 0.45;
    const artworks_container = document.getElementById('artworks');
    const artworks = document.getElementsByClassName('artwork');

    for (let i = 0; i < artworks.length; i++) {
        const artwork_img = artworks[i].getElementsByTagName('img')[0];
        const original_width = artwork_img.getAttribute('original-width');
        const original_height = artwork_img.getAttribute('original-height');

        artwork_img.width = Math.floor(artworks_container.clientWidth * percent_width);

        const multiplier = artwork_img.width / original_width;

        artwork_img.height = Math.floor(original_height * multiplier);
    }
}
document.addEventListener('DOMContentLoaded', resizeMedia);
window.addEventListener('resize', resizeMedia);
