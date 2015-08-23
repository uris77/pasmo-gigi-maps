(ns pasmo-gigi.geo.server
  (:gen-class)
  (:require [cheshire.generate :as cheshire.generate]
            [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [org.httpkit.server :refer [run-server]]
            [pasmo-gigi.geo.db.core :refer [disconnect! mongo-connection!]]
            [pasmo-gigi.geo.handlers :refer [app]]))

(defn start-server
  [handler port]
  (cheshire.generate/add-encoder org.bson.types.ObjectId cheshire.generate/encode-str)
  (let [server (run-server handler {:port port :join? false})]
    (log/info "Started Server on port " port)
    (mongo-connection!)
    server))

(defn stop-server
  [server]
  (when server
    (do
      (disconnect!)
      (server)))) ;; run-server returns a function that stops itself.

(defrecord PasmoGigiGeo []
  component/Lifecycle
  (start [this]
    (let [port (Integer/parseInt (or (System/getenv "PORT") "3449"))] 
      (assoc this :server (start-server #'app port))))
  (stop [this]
    (stop-server (:server this))
    (dissoc this :server)))

(defn create-system
  []
  (PasmoGigiGeo.))

(defn -main
  [& args]
  (.start (create-system)))

