(ns pasmo-gigi.geo.ui.routes
  (:require [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [re-frame.core :as re-frame])
  (:import goog.History)
  (:require-macros [secretary.core :refer [defroute]]))

(defn hook-browser-navigation!
  []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes
  []
  (secretary/set-config! :prefix "#")
  
  (defroute "/"
    []))

