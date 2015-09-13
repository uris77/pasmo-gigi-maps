(ns pasmo-gigi.geo.ui.map.map-views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :as r]
            [pasmo-gigi.geo.ui.db :refer [settings]]))

(def FEATURE-LAYER (r/atom nil))

(defn render-map
  []
  (let [mapbox-api (:mapbox-api @settings)
        map-el (.map (-> mapgox-api .-mapbox) "map" "uris77.nd0o07dd")
        feature-layer (.addTo (-> mapbox-api
                                  .-mapbox
                                  .featureLayer) map-el)]
    (reset! FEATURE-LAYER feature-layer)))

(defn map-coords
  [coords]
  (.setGeoJSON @FEATURE-LAYER (clj->js coords)))
