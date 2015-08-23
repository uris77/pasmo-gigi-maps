(ns pasmo-gigi.geo.db.locations
  (:require  [pasmo-gigi.geo.db.core :refer [mongo-connection!]]
             [monger.collection :as coll]))

(def HOTSPOT-COLOR "#BE1162")
(def TRADITIONAL-COLOR "#11A132")
(def NON-TRADITIONAL-COLOR "#10BB33")

(def mconn (mongo-connection!))
(def db (:db mconn))
(def conn (:conn mconn))
(def locations-coll "pasmo_locations")

(defn- marker-color
  [location-type]
  (case location-type
    "Hotspot" HOTSPOT-COLOR
    "traditional" TRADITIONAL-COLOR
    "Traditional" TRADITIONAL-COLOR
    "Non-Traditional" NON-TRADITIONAL-COLOR))

(defn all-available
  []
  (let [locations (coll/find-maps db locations-coll {:deleted false})]
    (map #(-> %
              (assoc :geometry (:loc %))
              (assoc :properties {:title (:name %)
                                  "marker-size" "large"
                                  "marker-color" (marker-color (:locationType %))
                                  "marker-symbol" "building"})
              (assoc :type "Feature"))
         locations)))

