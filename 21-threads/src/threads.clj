(ns threads)

(defn describe-number [n]
  (cond-> []
          (< n 0) (conj "negative")
          (> n 0) (conj "positive")
          (zero? n) (conj "zero")))

(defn -main []
  (-> 3
      (- 4)
      identity
      println)
  (->> 3
      (- 4)
      (identity)
      (println))
  (as-> 3 $
        (- 4 $)
        (identity $)
        (println $))
  (println (some-> 3
                   (- 4)
                   ((constantly nil))
                   ((constantly 5))))
  (println (some->> 3
                    (- 4)
                    ((constantly 5))))
  (println (describe-number (- 3 4))))
