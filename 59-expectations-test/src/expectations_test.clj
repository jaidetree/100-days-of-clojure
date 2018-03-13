(ns expectations-test
  (:require [expectations :refer :all]
            [clojure.test :refer :all]))

(deftest unexpected-test
  (testing "Retuns a vector with unexpected arg"
    (is (= (unexpected true) [:either true :expected :exception]))))

(deftest expected-test
  (testing "Returns a vector with expected arg"
    (is (= (expected true) [:either :unexpected true :exception]))))

(deftest exception-test
  (testing "Returns a vector with exception arg"
    (is (= (exception true) [:either :unexpected :expected true]))))

(deftest maybe-test
  (testing "Returns expected if value is truthy"
    (is (= (maybe true) [:either :unexpected true :exception])))
    (is (= (maybe 0) [:either :unexpected 0 :exception]))
    (is (= (maybe "hello") [:either :unexpected "hello" :exception]))
  (testing "Returns unexpected if value is falsey"
    (is (= (maybe false) [:either false :expected :exception])))
    (is (= (maybe nil) [:either nil :expected :exception]))
  (testing "Returns unexpected if value is exception"
    (let [err (Exception. "Error")]
      (is (= (maybe err) [:either err :expected :exception])))))

(deftest either?-test
  (testing "Returns true if value == :either"
    (is (= (either? :either) true))))

(deftest unexpected?-test
  (testing "Returns true if unexpected list"
    (is (= (unexpected? [:either true :expected :exception]) true))))

(deftest expected?-test
  (testing "Returns true if expected list"
    (is (= (expected? [:either :unexpected :expected :exception]) true))))

(deftest exception?-test
  (testing "Returns true if exception list"
    (is (= (exception? [:either :unexpected :expected true]) true))))

(deftest to-unexpected-test
  (testing "Returns transformed unexpected value"
    (is (= (to-unexpected inc [:either 0 :expected :exception])
           [:either 1 :expected :exception])))
  (testing "Returns curried function if only given one arg"
    (is (= ((to-unexpected inc) [:either 0 :expected :exception])))
           [:either 1 :expected :exception]))


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
