(ns pasmo-gigi.geo.ui.navigation.nav-handlers
  (:require [re-frame.core :refer [register-handler dispatch]]
            [reagent.core :as r]
            [cljs-http.client :as http]
            [pasmo-gigi.geo.ui.map.map-views :refer [map-coords]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def app-state {:surveys        []
                :survey-details {:type "FeatureCollection" :features []}
                :rendered-map?  false})


(defn fetch-surveys
  []
  (go 
    (let [resp (<! (http/get "/api/surveys" {"accept" "application/json"}))
          surveys (->> (:body resp)
                       (map #(assoc % :css "collection-item")))]
      (dispatch [:received-surveys surveys]))))

(defn fetch-survey-details
  [survey]
  (go
    (let [year  (:year survey)
          month (:month survey)
          url   (str "/api/survey-details?year=" year "&month=" month)
          resp  (<! (http/get url {"accept" "application/json"}))]
      (dispatch [:received-survey-deatils (get-in resp [:body :features])]))))

(defn remove-coords
  [coords coord-to-remove]
  (remove (fn [coord]
            (= (get-in coord [:survey :id]) (:_id coord-to-remove))) 
          coords))


(defn find-updated-surveys
  "Finds surveys that have been updated and toggles its
  css class.
  If a survey matches the year and month, but it 
  is not 'active', then an xhr request is made
  to fetch that survey's features; and an `active`
  css class is attached to it. If the survey is `active`, then
  the css class `active` is removed from it."
  [surveys year month]
  (map (fn [survey]
         (if (and (= year (:year survey))
                  (= month (:month survey)))
           (if (= "collection-item active" (:css survey))
             (assoc survey :css "collection-item")
             (do (fetch-survey-details survey)
                 (assoc survey :css "collection-item active")))
           survey))
       surveys))

(defn get-final-features
  "Removes all features for the surveys that are `unselected`.
  The survey's css is not changed yet, and it is active, so we 
  need to check for that and the survey's year and month to determine
  which feature to remove."
  [{:keys [surveys features year month]}]
  (reduce (fn [acc survey]
            (if (and (= year (:year survey))
                     (= month (:month survey))
                     (= "collection-item active" (:css survey)))
              (remove-coords acc survey)
              acc))
          features
          surveys))

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
   (let [year            (:year survey)
         month           (:month survey)
         surveys         (:surveys app-db)
         features        (get-in app-db [:survey-details :features])
         updated-surveys (find-updated-surveys surveys year month)
         final-features  (get-final-features {:year year :month month :features features :surveys surveys})]
     (map-coords final-features)
     (-> app-db
         (assoc-in [:survey-details :features] final-features)
         (assoc :surveys updated-surveys)))))

(register-handler
 :received-survey-deatils
 (fn [app-db [_ features]]
   (let [geo-json (concat (get-in app-db [:survey-details :features]) features)]
     (map-coords geo-json)
     (assoc-in app-db 
                [:survey-details :features]  
                geo-json))))
