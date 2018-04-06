(ns floor)

(def floor-chars {0 "_"
                  1 "-"})

(defn calc-block
  [_]
  (let [r (inc (rand-int 20))]
    (if (< r 19)
      0
      1)))

(defn gen-floor
  [max-width]
  (->> (range max-width)
       (map calc-block)
       (map #(get floor-chars %))
       (into [])))

(defn -main []
  (println (apply str (gen-floor 80))))
