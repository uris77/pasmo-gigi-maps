(ns pasmo-gigi.geo.handlers
  (:require [compojure
             [core :refer [defroutes GET routes]]
             [route :refer [resources]]]
            [environ.core :refer [env]]
            [pasmo-gigi.geo.db
             [locations :refer [all-available]]
             [surveys :as surveys]]
            [ring.middleware
             [defaults :refer [site-defaults wrap-defaults]]
             [json :refer [wrap-json-body wrap-json-response]]]
            [selmer.parser :refer [render-file]]))

(def json-resp {:headers {"Content-Type" "application/json"}})

(defn index
  [req]
  (render-file "templates/index.html" {:dev? (env :dev?)}))

(defn fetch-survey-details
  [req]
  (let [year  (Integer. (get-in req [:params :year]))
        month (get-in req [:params :month])
        survey-details {:type     "FeatureCollection" 
                        :features (surveys/all-survey-details-for year month)}]
    (assoc json-resp :body survey-details)))

(defroutes site
  (GET "/" req index)
  (resources "/"))

(defroutes api
  (GET "/api/locations" req
       (let [locations (all-available)]
         (assoc json-resp :body {:type "FeatureCollection" :features locations})))

  (GET "/api/surveys" _
       (let [surveys (surveys/all-survey-dates)]
         (assoc json-resp :body surveys)))

  (GET "/api/survey-details" req
       (wrap-json-body fetch-survey-details {:keyword? true})))

(def api-app
  (-> api
      wrap-json-body
      wrap-json-response))

(def app
  (wrap-defaults (routes api-app site) (assoc-in site-defaults [:security :anti-forgery] false)))


