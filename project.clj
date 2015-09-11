(defproject pasmo-gigi-maps "1.1.0-SNAPSHOT"
  :description "Plot outlets of gigi surveys on a map."
  :url "http://outletmappings.pasmo.bz"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.122"]
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

                 [com.stuartsierra/component "0.2.3"]
                 [reloaded.repl "0.1.0"]
                 [compojure "1.4.0"]
                 [cljs-http "0.1.37"]
                 [reagent "0.5.1-rc2"]
                 [reagent-utils "0.1.5"]
                 [re-frame "0.5.0-SNAPSHOT"]
                 [secretary "1.2.3"]]

  :jvm-opts ["-Xmx512m"]
  
  :plugins [[lein-ring "0.9.6"]
            [lein-environ "1.0.0"]]

  :main pasmo-gigi.geo.server

  :min-lein-version "2.0.0"
  
  :uberjar-name "pasmo-gigi-maps.jar"

  :profiles {:dev-common       {:plugins       [[lein-cljsbuild "1.0.6"]
                                                [lein-figwheel "0.3.7"]]
                                :dependencies  [[reloaded.repl "0.1.0"]]
                                :env           {:dev? true}
                                :open-browser? true
                                :source-paths ["dev" "src"]
                                :cljsbuild     {:builds [{:source-paths ["src/pasmo_gigi/geo/ui"]
                                                          :figwheel     true
                                                          :compiler     {:output-to "target/classes/public/js/app.js"
                                                                         :output-dir "target/classes/public/js/out"
                                                                         :asset-path "js/out"
                                                                         :optimizations :none
                                                                         :recompile-dependents true
                                                                         :main "pasmo-gigi.geo.ui.core"
                                                                         :foreign-libs [{:file "resources/public/js/mapbox.js"
                                                                                         :provides ["mapbox.L"]}]
                                                                         :source-map true}}]}}
             :dev-env-vars     {}
             :dev              [:dev-env-vars :dev-common]
             
             :uberjar-common   {:aot          :all
                                :omit-source  true
                                :source-paths ["src"]
                                :main         pasmo-gigi.geo.server
                                :env          {:dev? false}
                                :hooks        [leiningen.cljsbuild]
                                :cljsbuild    {:builds {:app {:source-paths ["src/pasmo_gigi/geo/ui"]
                                                              :jar          true
                                                              :figwheel     false
                                                              :output-wrapper false
                                                              :compiler     {:optimizations :advanced
                                                                             :main "pasmo-gigi.geo.ui.core"
                                                                             :output-to "target/classes/public/js/app.js"
                                                                             :output-dir "target/classes/public/js/out"
                                                                             :foreign-libs [{:file "resources/public/js/mapbox.js"
                                                                                             :provides ["mapbox.L"]}]}}}}}
             :uberjar-env-vars {:mongo-uri      (System/getenv "MONGO_URI")
                                :db             (System/getenv "DB")
                                :default-admin  (System/getenv "DEFAULT_ADMIN")
                                :client-id      (System/getenv "CLIENT_ID")
                                :client-secret  (System/getenv "CLIENT_SECRET")
                                :oauth-callback (System/getenv "OAUTH_CALLBACK")
                                :auth-url       (System/getenv "AUTH_URL")
                                :token-url      (System/getenv "TOKEN_URL")
                                :profile-url    (System/getenv "PROFILE_URL")}
             :uberjar          [:uberjar-common :uberjar-env-vars]})


