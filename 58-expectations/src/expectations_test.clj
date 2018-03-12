(ns expectations-test
  (:require [expectations :refer :all]
            [clojure.test :refer :all]))

(deftest combination-test
  (testing "Maybe expected combines with on-expected"
    (is (= (->> (maybe "hello world")
                (on-expected (constantly "cool"))
                (on-unexpected  (constantly "bad")))
           [:either :unexpected "cool" :exception])))
  (testing "Unexpected combines with to-unexpected to return an unexpected value"
    (is (= (->> (unexpected "an error")
                (on-expected (constantly "cool"))
                (to-unexpected  (constantly "bad")))
           [:either "bad" :expected :exception])))
  (testing "Maybe with exception can be transformed by to-unexpected"
    (is (= (->> (maybe (Exception. "another error"))
                (on-expected (constantly "cool"))
                (to-unexpected  (constantly "bad")))
           [:either "bad" :expected :exception])))
  (testing "Maybe with exception can be transformed to expected with on-unexpected"
    (is (= (->> (maybe (Exception. "another error"))
                (on-expected (constantly "cool"))
                (on-unexpected (constantly "fine")))
           [:either :unexpected "fine" :exception])))
  (testing "Explicit exception path is not operated on by on-expected or on-unexpected"
    (is (= (->> (exception :serious-error)
                (on-expected (constantly "cool"))
                (on-unexpected  (constantly "bad")))
           [:either :unexpected :expected :serious-error]))))

(defn -main []
  (run-tests 'expectations-test))
