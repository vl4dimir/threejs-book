(ns threejs-book.core
  (:require [threejs-book.ch1a :as ch1a]
            [threejs-book.ch6a :as ch6a]))

(defn main []
  ;;(ch1a/main)
  (ch6a/main))

(enable-console-print!)
(main)