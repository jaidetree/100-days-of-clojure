(ns either-preds
  (:require [clojure.test :refer :all]))

(def either-keys #{:either :expected :unexpected :exception})

(defn unexpected
  "RetUrns an either with a unexpected (unexpected) value"
  [arg & args]
  [:either arg :expected :exception])

(defn expected
  "Returns an either with an expected (expected) value."
  [arg & args]
  [:either :unexpected arg :exception])

(defn exception
  "Returns an either with an exception value."
  [arg]
  [:either :unexpected :expected arg])

(defn either?
  "Returns true if input is an either [:either ...] type"
  [input]
  (and (vector? input)
       (= (first input) :either)))

(defn get-value-type
  [input]
  (and (either? input)
       (let [values (set input)]
         (first (filter #(not (contains? values %))
                        either-keys)))))

(defn unexpected?
  [input]
  (= (get-value-type input) :unexpected))

(defn expected?
  [input]
  (= (get-value-type input) :expected))

(defn exception?
  [input]
  (= (get-value-type input) :exception))

(deftest get-value-type-test
  (testing "Returns correct type keywrod"
    (is (= (get-value-type (unexpected :hi)) :unexpected))
    (is (= (get-value-type (expected :hi)) :expected))
    (is (= (get-value-type (exception :hi)) :exception)))
  (testing "Predicates return true on correct keywords"
    (is (unexpected? (unexpected :hi)))
    (is (expected? (expected :hi)))
    (is (exception? (exception :hi))))
  (testing "Predicates return false on bad inputs"
    (is (not (unexpected? nil)))
    (is (not (expected? [:whatever])))
    (is (not (exception? {:you :suck})))))

(defn -main
  []
  (run-tests 'either-preds))

(-main)
