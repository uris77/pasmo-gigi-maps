(ns pasmo-gigi.geo.db.surveys
  (:require [pasmo-gigi.geo.db.core :refer [mongo-connection!]]
            [monger.collection :as coll]))

(def mconn (mongo-connection!))
(def db (:db mconn))
(def conn (:conn mconn))
(def surveys-coll "pasmo_surveys")
(def surveys-details-coll "outlet_surveys")

;;; COLORS
(def RED "#BE1162")
(def GREEN "#11A132")

(defn sort-by-date
  [surveys]
  (vec (reverse (sort-by (juxt :year :monthOrder) surveys))))

(defn all-survey-dates
  "Get a list of all surveys that were conducted."
  []
  (->> (coll/find-maps db surveys-coll)
       sort-by-date))

(defn marker-color
  [survey]
  (if (:condomsAvailable survey)
    GREEN
    RED))

(defn all-survey-details-for
  "Extract the surveys for every location conducted on year & month."
  [year month]
  (let [outlet-surveys (coll/find-maps db surveys-details-coll {:survey.year year :survey.month month})]
    (->> outlet-surveys 
         (map #(let [loc (get-in % [:location :loc])]
                 (assoc % :geometry loc)))
         (map #(-> %
                   (assoc :properties {:title (get-in % [:location :name])
                                       :marker-size "large"
                                       :marker-symbol "building"
                                       :marker-color (marker-color %)})
                   (assoc :type "Feature")))
         (map #(select-keys % [:_id :lubesAvailable :survey :geometry :type :condomsAvailable :properties])))))


