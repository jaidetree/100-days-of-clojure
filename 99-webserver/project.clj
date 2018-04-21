(defproject webserver "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "3.8.0"]
                 [cheshire "5.8.0"]]
  :plugins [[lein-ring "0.12.4"]]
  :main ^:skip-aot webserver.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :ring {:handler webserver.core/handler})
