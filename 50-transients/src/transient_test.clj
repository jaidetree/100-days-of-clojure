(ns transient-test)

(defn -main []
  (let [v (transient {})
        f (transient [])]
   (assoc! v :hello "world")
   (conj! f "Yoanna")
   (conj! f "Ben")
   (assoc! v :friends (persistent! f))
   (println (persistent! v))))
