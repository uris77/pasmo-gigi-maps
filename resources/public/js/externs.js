var L = {mapbox: {
    map: function(el, key) {},
    accessToken: "",
    featureLayer: function() {
        return {addTo: function(map) {
            return {setGeoJSON: function(coords) {}};
        }};        
      }
    }
};

