(ns pidgeon-holer-test
  (:require [pidgeon-holer :refer :all]
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
    (is (= (either? [:either]) true))))

(deftest unexpected?-test
  (testing "Returns true if unexpected list"
    (is (= (unexpected? [:either true :expected :exception]) true))))

(deftest expected?-test
  (testing "Returns true if expected list"
    (is (= (expected? [:either :unexpected :hi :exception]) true))))

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

(deftest on-unexpected-test
  (testing "Returns expected value from unexpected value"
    (is (= (on-unexpected inc (unexpected 0))
           [:either :unexpected 1 :exception])))
  (testing "Returns curried function if only given one arg"
    (is (= ((on-unexpected inc) (unexpected 0))
           [:either :unexpected 1 :exception]))))

(deftest on-expected-test
  (testing "Returns expected value"
    (is (= (on-expected inc (expected 0))
           [:either :unexpected 1 :exception])))
  (testing "Returns curried function if only given one arg"
    (is (= ((on-expected inc) (expected 0))
           [:either :unexpected 1 :exception]))))

(deftest to-exception-test
  (testing "Returns transformed unexpected value"
    (is (= (to-exception inc (exception 0))
           [:either :unexpected :expected 1])))
  (testing "Returns curried function if only given one arg"
    (is (= ((to-exception inc) (exception 0))
           [:either :unexpected :expected 1]))))

(deftest on-exception-test
  (testing "Returns expected value from exception value"
    (is (= (on-exception inc (exception 0))
           [:either :unexpected 1 :exception])))
  (testing "Returns curried function if only given one arg"
    (is (= ((on-exception inc) (exception 0))
           [:either :unexpected 1 :exception]))))

(deftest when-unexpected-test
  (testing "Returns updated expectation when predicate returns true"
    (is (= ((when-unexpected identity inc) (unexpected 1))
           [:either :unexpected 2 :exception]))
    (is (= ((when-unexpected identity inc) (unexpected nil))
           [:either nil :expected :exception]))))

(deftest when-expected-test
  (testing "Returns updated expectation when predicate returns true"
    (is (= ((when-expected identity inc) (expected 1))
           [:either :unexpected 2 :exception]))
    (is (= ((when-expected identity inc) (expected nil))
           [:either :unexpected nil :exception]))))

(deftest get-value-test
  (testing "Returns the value of the given either"
    (is (= (get-value (expected :hi)) :hi))
    (is (= (get-value (unexpected :hi)) :hi))
    (is (= (get-value (exception :hi)) :hi))))

(deftest pipe-test
  (testing "Returns the result of applying the given f to the value of the either"
    (is (= (pipe inc (expected 0)) 1))
    (is (= (pipe inc (unexpected 0)) 1))
    (is (= (pipe inc (exception 0)) 1))))

(deftest pipe-expected-test
  (testing "Returns the result of applying function to expected value"
    (is (= (pipe-expected inc (expected 0)) 1))
    (is (= (pipe-expected inc (unexpected 0)) (unexpected 0)))))

(deftest pipe-unexpected-test
  (testing "Returns the result of applying function to unexpected value"
    (is (= (pipe-unexpected inc (unexpected 0)) 1))
    (is (= (pipe-unexpected inc (expected 0)) (expected 0)))))

(deftest pipe-exception-test
  (testing "Returns the result of applying function to exception value"
    (is (= (pipe-exception inc (exception 0)) 1))
    (is (= (pipe-exception inc (unexpected 0)) (unexpected 0)))))

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
  (run-tests 'pidgeon-holer-test))

(-main)
