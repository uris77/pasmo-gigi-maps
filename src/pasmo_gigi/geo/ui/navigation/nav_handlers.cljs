(ns pasmo-gigi.geo.ui.navigation.nav-handlers
  (:require [re-frame.core :refer [register-handler dispatch]]
            [reagent.core :as r]
            [cljs-http.client :as http]
            [pasmo-gigi.geo.ui.map.map-views :refer [map-coords]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def app-state {:surveys        []
                :survey-details []
                :rendered-map?  false})


(defn fetch-surveys
  []
  (go 
    (let [resp (<! (http/get "/api/surveys" {"accept" "application/json"}))
          surveys (->> (:body resp)
                       (map #(assoc % :css "collection-item")))]
      (dispatch [:received-surveys surveys]))))

(defn validate-geojson
  [coords]
  (go
    (let [resp (<! (http/post "http://geojsonlint.com/validate" {:json-params coords}))]
      (.log js/console "validate: " (clj->js resp)))))

(defn fetch-survey-details
  [survey]
  (go
    (let [year  (:year survey)
          month (:month survey)
          url   (str "/api/survey-details?year=" year "&month=" month)
          resp  (<! (http/get url {"accept" "application/json"}))]
      (dispatch [:received-survey-deatils (:body resp)]))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;; Handlers
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(register-handler
 :initialize-db 
 (fn [_ _]
   (fetch-surveys)
   app-state))

(register-handler
 :received-surveys
 (fn [app-db [_ surveys]]
   (assoc app-db :surveys surveys)))

(register-handler
 :fetch-survey-details
 (fn [app-db [_ survey]]
   (let [updated-surveys (map (fn [it]
                                (let [year  (:year survey)
                                      month (:month survey)]
                                  (if (and (= year (:year it)) (= month (:month it)))
                                    (if (= "collection-item active" (:css it))
                                      (assoc it :css "collection-item")
                                      (do 
                                        (fetch-survey-details survey)
                                        (assoc it :css "collection-item active")))
                                    (assoc it :css "collection-item"))))
                              (:surveys app-db))]

     (assoc app-db :surveys updated-surveys))))

(register-handler
 :received-survey-deatils
 (fn [app-db [_ details]]
   (map-coords details)
   (assoc app-db :survey-details details)))
