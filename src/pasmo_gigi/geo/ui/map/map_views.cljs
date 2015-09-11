(ns pasmo-gigi.geo.ui.map.map-views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :as r]
            [mapbox.L :as L]))

(def FEATURE-LAYER (r/atom nil))

(defn render-map
  []
  (let [map-el (.map (-> js/L .-mapbox) "map" "uris77.nd0o07dd")
        feature-layer (.addTo (-> js/L
                                  .-mapbox
                                  .featureLayer) map-el)]
    (reset! FEATURE-LAYER feature-layer)))

(defn map-coords
  [coords]
  (.setGeoJSON @FEATURE-LAYER (clj->js coords)))
