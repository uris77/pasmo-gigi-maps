(ns pasmo-gigi.geo.ui.db
  (:require [reagent.core :as r]))

(def settings (r/atom {:mapbox-api nil}))
