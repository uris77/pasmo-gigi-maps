(ns pasmo-gigi.geo.ui.navigation.nav-views
  (:require [re-frame.core :refer [subscribe dispatch]]))

(defn- on-click-survey
  [month year]
  (dispatch [:fetch-survey-details {:month month :year year}]))

(defn nav-view
  []
  (let [surveys (subscribe [:surveys])]
    (fn []
      [:div.collection.with-header
       [:div.collection-header [:h4 "Surveys"]]
       (for [survey @surveys]
         (let [survey-date (str (:month survey) " " (:year survey))]
           ^{:key survey} [:a {:href     "#!"
                               :class    (:css survey)
                               :on-click #(on-click-survey (:month survey) (:year survey))} survey-date]))])))

