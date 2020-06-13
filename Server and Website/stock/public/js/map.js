var map;
var marker;
/**
 * Calls, generates and initializes the Google Map
 */
function initMap() {
    // The location of USA
    var usa = {lat: 39.50, lng: -98.35};
    // The map, centered at USA
    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 2,
        center: new google.maps.LatLng(30, 0),
    });
    marker = new google.maps.Marker({
        map: null,
        position: null
    });
}

