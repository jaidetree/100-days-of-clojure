(ns fn-util-test
  (:require [fn-util :refer :all]
            [clojure.test :refer :all]))

(deftest tap-test
  (testing "Will do a side effect without changing the result"
    (is (= (->> (range 3)
                (map (tap println))
                (map #(* 2 %)))
           [0 2 4]))))

(defn -main []
  (run-tests 'fn-util-test))
