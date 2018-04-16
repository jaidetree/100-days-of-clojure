(ns loop-accum)

;; Quick practice to think through how to draw shapes
;; with quill using accumulating numbers

(defn -main
  []
  (doseq [x (range 20 221 200)]
    (doseq [y (range 20 221 200)]
      (println {:type :circle :r 200 :x x :y y}))))
