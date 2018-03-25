(defproject mta "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [com.google.transit/gtfs-realtime-bindings "0.0.4"]
                 [clj-http "3.7.0"]
                 [cheshire "5.8.0"]
                 [com.google.protobuf/protobuf-java "3.5.1"]]
  :source-paths      ["src/clojure"]
  :java-source-paths ["src/java"]
  :main ^:skip-aot mta.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
