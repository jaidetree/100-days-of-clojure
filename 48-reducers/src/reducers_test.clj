(ns reducers-test
  (:require [clojure.core.reducers :as r]))

(def items [{:name "Ketchup"
             :price 5.24}
            {:name "Pizza"
             :price 12.42}
            {:name "Burgers"
             :price 21.63}])

;; Resources
;; https://clojure.org/reference/reducers

;; PROs
;; + Can be done in parallel
;; + Efficient eager application of a multi-step transformation
;; Avoids dangling I/O resource issues

(defn sum
  [coll]
  (->> coll
       (r/map :price)
       ;; uses parallel reduce + cmobine
       (r/fold +)))

(defn -main []
  (println (sum items)))
