(ns floor-stream)

(def floor-chars {0 "_"
                  1 "-"})

(defn gen-tile
  []
  (let [r (inc (rand-int 20))]
    (Thread/sleep 250)
    (if (< r 19) 0 1)))

(defn floor-seq
  []
  (repeatedly gen-tile))

(defn -main
  []
  (->> (floor-seq)
       (map #(get floor-chars %))
       (partition 20)
       (map println)
       (take 3)))

(-main)
