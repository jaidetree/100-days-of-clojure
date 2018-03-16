(ns destruct
  (:require [clojure.test :refer :all]))

(deftest vector-destruct-test
  (testing "Can destruct vector into items"
    (let [[x y z] [1 2 3]]
     (is (= x 1))
     (is (= y 2))
     (is (= z 3))))
  (testing "Can destruct vector into head and tail"
    (let [[head & tail] [1 2 3]]
      (is (= head 1))
      (is (= (first tail) 2))
      (is (= (second tail) 3))))
  (testing "Can destruct vector into head, tail, and all"
    (let [[head & tail :as args] [1 2 3]]
      (is (= head 1))
      (is (= (first tail) 2))
      (is (= (second tail) 3))
      (is (= args [1 2 3]))))
  (testing "Can destruct strings into characters"
     (let [[a b c] "abc"]
      (is (= a \a))
      (is (= b \b))
      (is (= c \c))))
  (testing "Elements can be ignored with _"
     (let [[x _ z] [1 2 3]]
       (is (= x 1))
       (is (= z 3)))))

(deftest map-destruct-test
  (testing "Can destruct map into values"
    (let [{x :x y :y z :z a :a} {:x 0 :y 1 :z 2}]
      (is (= x 0))
      (is (= y 1))
      (is (= z 2))
      (is (= a nil))))
  (testing "Can destruct with default values"
     (let [{x :x y :y :or {y 1}} {:x 0}]
      (is (= x 0))
      (is (= y 1))))
  (testing "Can destruct with :all"
     (let [{x :x y :y :as all} {:x 0 :y 1}]
       (is (= x 0))
       (is (= y 1))
       (is (= all {:x 0 :y 1}))))
  (testing "Can destruct with :keys, :strs, and :syms list"
    (let [{:keys [x y z]} {:x 0 :y 1 :z 2}]
      (is (= x 0))
      (is (= y 1))
      (is (= z 2)))
    (let [{:strs [x y z]} {"x" 0 "y" 1 "z" 2}]
      (is (= x 0))
      (is (= y 1))
      (is (= z 2)))
    (let [{:syms [x y z]} {'x 0 'y 1 'z 2}]
      (is (= x 0))
      (is (= y 1))
      (is (= z 2))))
  (testing "Can destruct rest args"
    (let [[x & {:keys [y z] :or {y 1 z 2}}] [0 :y 2 :z 3]]
      (is (= x 0))
      (is (= y 2))
      (is (= z 3)))
    (let [[x & {:keys [y z] :or {y 1 z 2}}] [0]]
      (is (= x 0))
      (is (= y 1))
      (is (= z 2)))))


(defn -main []
  (run-tests 'destruct))
