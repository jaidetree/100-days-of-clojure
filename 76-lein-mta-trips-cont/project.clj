(defproject mta "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/core.async "0.4.474"]
                 [proto-repl "0.3.1"]]
  :source-paths      ["src/clojure"]
  :java-source-paths ["src/java"]
  :main ^:skip-aot mta.stations
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
