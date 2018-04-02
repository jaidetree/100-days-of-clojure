(ns trampoline-test
  (:require [clojure.test :refer :all]))

(defn counter
  ([]
   #(counter 0))
  ([x]
   (println x)
   (if (>= x 100)
     x
     #(counter (inc x)))))

(deftest counter-test
  (testing "Counter will recursively loop until a function is not returned"
    (is (= (trampoline counter) 100))))

(defn -main
  []
  (run-tests 'trampoline-test))
