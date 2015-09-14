var L = {mapbox: {}};

L.mapbox.map = function(el, key) {};
L.mapbox.accessToken = "";
L.mapbox.featureLayer = function() {
    return {addTo: function(map) {
        return {setGeoJSON: function(coords) {}};
    }};
};
