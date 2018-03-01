(ns comp)

(defn my-comp
  [& fns]
  (fn [v]
    (loop [result v
           [f & rest-fns] fns]
      (if (empty? rest-fns)
        (f result)
        (recur (f result) rest-fns)))))

(defn -main []
  (println (= ((my-comp :a :b) {:a {:b 5}}) 5)))
