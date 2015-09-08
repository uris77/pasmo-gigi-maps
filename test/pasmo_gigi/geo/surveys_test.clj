(ns pasmo-gigi.geo.surveys-test
  (:require  [clojure.test :refer :all]
             [pasmo-gigi.geo.db.surveys :as surveys]))

(def surveys-list [{:_id "1" :month "February" :monthOrder 2 :year 2015}
                   {:_id "0" :month "October" :monthOrder 10 :year 2014}
                   {:_id "2" :month "December" :monthOrder 12 :year 2014}
                   {:_id "3" :month "March" :monthOrder 3 :year 2015}])

(def ordered-surveys-list [{:_id "3" :month "March" :monthOrder 3 :year 2015}
                           {:_id "1" :month "February" :monthOrder 2 :year 2015}
                           {:_id "2" :month "December" :monthOrder 12 :year 2014}
                           {:_id "0" :month "October" :monthOrder 10 :year 2014}])

(deftest orders-surveys-test
  (let [reversed (surveys/sort-by-date surveys-list)]
    (clojure.pprint/pprint reversed)
    (is (= reversed ordered-surveys-list))))
