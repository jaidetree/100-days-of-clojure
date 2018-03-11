(ns either-test
  (:require [either :refer :all]
            [clojure.test :refer :all]))

(deftest ui-test
  (testing "Make sure UI works expected with real test scenario"
    (is (= (->> (ok "hello world")
                (to-right (constantly "cool"))
                (on-left  (constantly "bad")))
           [:either :left "cool" :exception]))
    (is (= (->> (err "an error")
                (to-right (constantly "cool"))
                (to-left  (constantly "bad")))
           [:either "bad" :right :exception]))
    (is (= (->> (ok (Exception. "another error"))
                (on-right (constantly "cool"))
                (to-left  (constantly "bad")))
           [:either "bad" :right :exception]))
    (is (= (->> (ok (Exception. "another error"))
                (on-right (constantly "cool"))
                (on-left (constantly "fine")))
           [:either :left "fine" :exception]))
    (is (= (->> (exception :serious-error)
                (on-right (constantly "cool"))
                (on-left  (constantly "bad")))
           [:either :left :right :serious-error]))))

(defn -main []
  (run-tests 'either-test))
