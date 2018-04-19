(ns mta.core
  (:require [mta.trips :as trips]
            [mta.stations :as mta-stations]
            [clojure.string :as str]
            [clojure.pprint :refer [print-table pprint]]))

(defn get-config
  [filename]
  (read-string (slurp filename)))

(defn parse-opts
  [args]
  (as-> args $
        (str/join " " $)
        (str/split $ #"--")
        (drop 1 $)
        (map #(str/split % #" " 2) $)
        (map #(vector (keyword (first %)) (str/trim (second %))) $)
        (into {} $)))

(defn where
  [query coll]
  (->> coll
       (filter (fn where-filter
                 [trip]
                 (every? true?
                         (map (fn query-map
                                [[key value]]
                                (= (get trip key) value)) query))))))


(defn -main
  [& args]
  (let [config (get-config "dev.secret.edn")
        stations (mta-stations/get-stations!)
        query (parse-opts args)]
    (->> (trips/get-trips! config stations)
         (where query)
         (sort-by :arrives)
         (take 4)
         (print-table [:train :direction :station :station-id :arrives :line :latitude :longitude]))))
