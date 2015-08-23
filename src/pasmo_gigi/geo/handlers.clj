(ns pasmo-gigi.geo.handlers
  (:require [compojure
             [core :refer [defroutes routes GET]]
             [route :refer [resources]]]
            [environ.core :refer [env]]
            [selmer.parser :refer [render-file]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [pasmo-gigi.geo.db.locations :refer [all-available]]))

(defn index
  [req]
  (render-file "templates/index.html" {:dev? (env :dev?)}))

(defroutes site
  (GET "/" req index)
  (resources "/"))

(defroutes api
  (GET "/api/locations" req
       (let [locations (all-available)]
         {:headers {"Content-Type" "application/json"}
          :body {:type "FeatureCollection" :features locations}})))

(def api-app
  (-> api
      wrap-json-body
      wrap-json-response))

(def app
  (wrap-defaults (routes api-app site) (assoc-in site-defaults [:security :anti-forgery] false)))

