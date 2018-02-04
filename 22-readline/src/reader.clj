(ns reader)

(defn count-pizzas
  [n-slices-str]
  (let [n-slices (Integer. n-slices-str)]
    (int (+ 0.5 (/ n-slices 8)))))

(defn -main []
  (println "How many pizza slices?")
  (println (str "Ok, you'll need " (count-pizzas (read-line)) " pizzas")))
