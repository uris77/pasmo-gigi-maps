(ns pasmo-gigi.geo.ui.navigation.nav-subscriptions
  (:require [re-frame.core :refer [register-sub]])
  (:require-macros [reagent.ratom :refer [reaction]]))

(register-sub
 :surveys
 (fn [app-db _]
   (reaction (:surveys @app-db))))

(register-sub
 :survey-details
 (fn [app-db _]
   (reaction (:survey-details @app-db))))

