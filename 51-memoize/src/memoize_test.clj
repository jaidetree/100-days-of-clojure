(ns memoize-test)

(def add (memoize (fn [x y] (+ x y))))

(defn -main
  []
  (time (println (str "1 + 2 = " (add 1 2))))
  (time (println (str "1 + 2 = " (add 1 2))))
  (time (println (str "3 + 4 = " (add 3 4))))
  (time (println (str "3 + 5 = " (add 3 5))))
  (time (println (str "3 + 5 = " (+ 3 5))))
  (time (println (str "3 + 5 = " (add 3 5)))))
