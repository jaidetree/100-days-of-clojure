(ns reader)

(declare pizza-prompt)

(defn slices->pies
  [n-slices-str]
  (try
    (let [n-slices (Long. n-slices-str)]
      (long (Math/ceil (/ n-slices 8))))
    (catch Exception e
      (println "I didn't catch that.")
      (pizza-prompt)
      nil)))

(defn pizza-prompt
  []
  (println "How many pizza slices?")
  (when-let [pizzas (slices->pies (read-line))]
    (println
     (cond
        (= pizzas 1) "Ok, 1 pizza 🍕  coming right up!"
        (> pizzas 125000) (str "Uhhh ain't nobody got time for that much pizza 🍕!")
        (> pizzas 1) (str "Ok, " pizzas " pizzas 🍕  coming right up!")
        :else "Fine. No pizza 🍕  for you!"))))

(defn -main []
  (pizza-prompt))
