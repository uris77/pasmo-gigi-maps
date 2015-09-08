(ns repl
  (:require [reloaded.repl :refer [system reset stop]]
            [pasmo-gigi.geo.server]))

(reloaded.repl/set-init! #'pasmo-gigi.geo.server/create-system)

