(ns mta.stations
  (:require [clojure.string :as str]
            [clojure.pprint :refer :all]))

(def station-keys '(:station-id
                    :complex-id
                    :gtfs-stop-id
                    :division
                    :line
                    :stop-name
                    :borough
                    :daytime-routes
                    :structure
                    :gtfs-latitude
                    :gtfs-longitude))

(defn read-stations-csv!
  [filename]
  (with-open [rdr (clojure.java.io/reader filename)]
    (reduce conj [] (line-seq rdr))))

(defn parse-station
  [station-row-str]
  (->> (zipmap station-keys (str/split station-row-str #","))
       (filter
        (fn [[k]] (contains? #{:gtfs-stop-id :stop-name :line} k)))
       (reduce conj {})))

(defn -main []
  (binding [*print-miser-width* 2
            *print-right-margin* 50]
    (->> (read-stations-csv! "stations.secret.csv")
         (drop 1)
         (map parse-station)
         (pprint)
         (dorun))))
