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
            [mapbox.L :as L])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(.log js/console "access token: " (-> js/L .-mapbox))

(let [mapbox (.-mapbox js/L)]
  (set! (-> mapbox .-accessToken) "pk.eyJ1IjoidXJpczc3IiwiYSI6InRuYTZRa3MifQ._Bo-JRcA7QVGocCJvdSoJg"))

(defn mount-root
  []
  (reagent/render [nav-views/nav-view]
                  (.getElementById js/document "nav")))

(defn ^:export init
  []
  (.log js/console "Starting...")
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root)
  (map-views/render-map))


(init)
