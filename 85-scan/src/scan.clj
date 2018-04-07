(ns scan)

(defn scan
  [f val col]
  (loop [results [val]
         [head & rest] col]
    (let [results (conj results (f (last results) head))]
      (if (not (empty? rest))
        (recur results rest)
        results))))

(defn -main
  []
  (->> (range 5)
       (scan + 0)
       (println)
       (dorun)))
