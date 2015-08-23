(defproject pasmo-gigi-maps "1.0.0"
  :description "Plot outlets of gigi surveys on a map."
  :url "http://outletmappings.pasmo.bz"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [environ "1.0.0"]
                 [compojure "1.4.0"]
                 [cheshire "5.5.0"]
                 [com.novemberain/monger "3.0.0"]
                 [selmer "0.8.7"]
                 [http-kit "2.1.18"]
                 [ring-server "0.4.0" :exclusions [org.eclipse.jetty/jetty-http
                                                   org.eclipse.jetty/jetty-continuation]]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-json "0.4.0"]
                 [com.cemerick/friend "0.2.1" :exclusions [ring/ring-core
                                                           org.clojure/core.cache
                                                           org.apache.httpcomponents/httpclient]]
                 [friend-oauth2 "0.1.3" :exclusions [commons-logging
                                                     org.apache.httpcomponents/httpcore]]
                 [com.stuartsierra/component ""]
                 [com.stuartsierra/component "0.2.3"]
                 [reloaded.repl "0.1.0"]
                 [compojure "1.4.0"]]
  
  :plugins [[lein-ring "0.9.6"]
            [lein-environ "1.0.0"]]

  :main pasmo-gigi.geo.server

  :min-lein-version "2.0.0"
  
  :uberjar-name "pasmo-gigi-maps.jar"

  :profiles {:dev-common   {:dependencies [[ring/ring-mock "0.2.0"] [ring/ring-devel "1.4.0"]]
                            :env {:dev? true}
                            :open-browser? true}

             :dev          [:dev-env-vars :dev-common]})


