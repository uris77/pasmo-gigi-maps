(ns pasmo-gigi.geo.ui.map.map-views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :as r]
            [goog.object :as gob]
            [pasmo-gigi.geo.ui.db :refer [settings]]))

(def FEATURE-LAYER (r/atom nil))

(defn render-map
  []
  (let [mapbox-api       (:mapbox-api @settings)
        mapbox-prop      (gob/get mapbox-api "mapbox")
        map-el           (.map mapbox-prop  "map" "uris77.nd0o07dd")
        feature-layer-fn ((gob/get mapbox-prop "featureLayer"))
        feature-layer    (.addTo feature-layer-fn map-el)]
    (reset! FEATURE-LAYER feature-layer)))

(defn map-coords
  [coords]
  (.setGeoJSON @FEATURE-LAYER (clj->js coords)))
