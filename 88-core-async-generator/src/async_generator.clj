(ns async-generator
  (:require [clojure.core.async :as async]))

(def tile-chars {1 "_"
                 0 "—"})

(defn get-tile
  [x]
  (if (> x 23) 1 0))

(defn get-tile-char
  [x]
  (get tile-chars x))

(defn create-tile-ch
  []
  (async/to-chan (->> (range)
                      (map (fn calc-tile [x] (rand-int 25)))
                      (map get-tile)
                      (map get-tile-char))))

(defn -main
  []
  (let [ch (create-tile-ch)]
    (async/<!! (async/go-loop []
                 (when-let [x (async/<! ch)]
                   (print x)
                   (recur))))))
