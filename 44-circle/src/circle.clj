(ns circle)

(defn is-circle?
  [radius x y]
  (== radius (Math/ceil (Math/sqrt (+ (* x x) (* y y))))))

(defn draw-circle
  [radius]
  (doseq [y (range 0 (inc (* radius 2)))]
    (doseq [x (range 0 (inc (* radius 2)))]
      (if (is-circle? radius (- x radius) (- y radius))
        (print "*")
        (print " "))
      (flush))
    (println "")))

(defn -main
  [r]
  (let [radius (Integer. r)]
    (draw-circle radius)))
