(defproject threejs-book "0.1.0-SNAPSHOT"
  :description "Practice project for going through the 'Learning Three.js' book."
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0-RC1"]
                 [org.clojure/clojurescript "0.0-3269"]
                 [lein-light-nrepl "0.1.0"]]

  :plugins [[lein-cljsbuild "1.0.6"]
            [lein-figwheel "0.3.3"]]

  :clean-targets ^{:protect false} [:target-path "out"]

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs"]
                        :figwheel {:on-jsload "threejs-book.core/main"}
                        :compiler {:main threejs-book.core}}]}

  :figwheel {:css-dirs ["css"]
             :nrepl-port 7888}

  :repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]})
