(ns transducers-test
    (:require [clojure.test :refer :all]))

;; Transducers are fired in reverse order
(def xf (comp
         (map inc)
         (filter odd?)
         (take 5)))
;; is equivalent to using ->> macro

(deftest comp-test
  (testing "Will transducing a range of 0 - 5 be 9"
    (is (= (transduce xf + (range 5)) 9))))

(deftest eduction-test
  (testing "Will transduce a range of 0 - 5 to 9"
    ;; What does eduction do?
    ;; It creates transducer that always emits returns ta reducible/iterable
    ;; that always emits applies the transducers xf to the coll.
    (is (= (reduce + 0 (eduction xf (range 5))) 9))))

(deftest into-test
  (testing "Will into return a list of numbers"
    (is (= (into [] xf (range 5)) [1 3 5]))
    (is (vector? (into [] xf (range 5))))
    (is (seq? (into '() xf (range 5))))))

(deftest sequence-test
  (testing "sequence will return a lazy sequence"
    (doseq [x (sequence xf (range 5))]
      (println (str "sequence-test x = " x)))
    (is (= (sequence xf (range 5)) [1 3 5]))))

(defn -main []
  (run-tests 'transducers-test))
