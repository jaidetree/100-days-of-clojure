(ns defrecord-test
  (:require [clojure.test :refer :all]))

(defrecord Either [exception unexpected expected])

(def greeting (Either. nil nil :hi))

(deftest is-either-immutable-test
  (testing "Is like a map"
    (is (= (:expected greeting) :hi)))
  (testing "Is immutable"
    (let [e (assoc greeting :expected :hello)]
      (is (= (:expected e) :hello))
      (is (not (= e greeting)))))
  (testing "Does allow extra keys being set"
    (is (= (:whatever (assoc greeting :whatever :oops))
           :oops))))

(defn -main []
  (run-tests 'defrecord-test))

(-main)
