(ns pasmo-gigi.geo.ui.core
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [pasmo-gigi.geo.ui.navigation.nav-handlers]
            [pasmo-gigi.geo.ui.navigation.nav-subscriptions]
            [pasmo-gigi.geo.ui.navigation.nav-views :as nav-views]
            [pasmo-gigi.geo.ui.map.map-views :as map-views]
            [pasmo-gigi.geo.ui.routes :as routes]
            [pasmo-gigi.geo.ui.db :as db])
  (:require-macros [cljs.core.async.macros :refer [go]]))


(defn setup-mapbox [mapbox-api]

  (.log js/console "mapbox api: " mapbox-api)

  (let [mapbox (.-mapbox mapbox-api)]
    (set! (.-accessToken mapbox) "pk.eyJ1IjoidXJpczc3IiwiYSI6InRuYTZRa3MifQ._Bo-JRcA7QVGocCJvdSoJg")
    (swap! db/settings assoc :mapbox-api mapbox-api))

  (.log js/console "set: " (.-accessToken (.-mapbox (:mapbox-api @db/settings)))))

(defn mount-root
  []
  (reagent/render [nav-views/nav-view]
                  (.getElementById js/document "nav")))

(defn ^:export init
  [mapbox-api]
  (.log js/console "Starting with ..." mapbox-api)
  (setup-mapbox mapbox-api)
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root)
  (map-views/render-map))


(init js/L)
