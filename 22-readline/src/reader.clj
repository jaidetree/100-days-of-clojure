(ns reader)

(declare pizza-prompt)

(defn count-pizzas
  [n-slices-str]
  (try
    (let [n-slices (Integer. n-slices-str)]
      (int (+ 0.5 (/ n-slices 8))))
    (catch Exception e
      (println "I didn't catch that.")
      (pizza-prompt)
      nil)))

(defn pizza-prompt
  []
  (println "How many pizza slices?")
  (when-let [pizzas (count-pizzas (read-line))]
    (println (str "Ok, you'll need " pizzas " pizzas"))))

(defn -main []
  (pizza-prompt))
