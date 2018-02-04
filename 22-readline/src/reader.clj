(ns reader)

(declare pizza-prompt)
(defn count-pizzas
  [n-slices-str]
  (try
    (let [n-slices (Integer. n-slices-str)]
      (int (Math/ceil (/ n-slices 8))))
    (catch Exception e
      (println "I didn't catch that.")
      (pizza-prompt)
      nil)))

(defn pizza-prompt
  []
  (println "How many pizza slices?")
  (when-let [pizzas (count-pizzas (read-line))]
    (println
     (cond
        (= pizzas 1) "Ok, 1 pizza 🍕  coming right up!"
        (> pizzas 1) (str "Ok, " pizzas " pizzas 🍕  coming right up!")
        :else "Fine. No pizza 🍕  for you!"))))

(defn -main []
  (pizza-prompt))
