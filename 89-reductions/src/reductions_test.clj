(ns reductions-test
  (:require [clojure.test :refer :all]))

;; Looks like I don't need to implement a scan function as it comes built in
;; under the name "reductions"

(deftest reductions-init-test
  (testing "Reduces incrementally with init value"
    (is (= (reductions + 0 (range 5))
           [0 0 1 3 6 10]))))

(deftest reductions-test
  (testing "Reduces incrementally without init value"
    (is (= (reductions + (range 5))
           [0 1 3 6 10]))))

(defn -main
  []
  (run-tests 'reductions-test))
