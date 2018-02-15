(ns lein-test.core-test
  (:require [clojure.test :refer :all]
            [lein-test.core :refer :all]))

(deftest str->int-test
  (testing "Can convert integer strings"
    (is (= (str->int "4") 4))))

(deftest add-5-test
  (testing "Can add 5 to a positive integer"
    (is (= (add-5 1) 6)))
  (testing "Can add 5 to 0 to get an identity"
    (is (= (add-5 0) 5))))
