(ns ansi-test
  (:require [clojure.test :refer :all]
            [ansi :refer :all]))

(deftest get-ansi-code-test
  (testing "Will return a ansi escape template if exists"
    (is (= (get-ansi-code :clear)
           [:ok "[2J"])))
  (testing "Will return an error if code does not exist"
    (is (= (get-ansi-code :blah)
           [:err "No ansi code found for :blah"]))))

(deftest print-ansi-test
  (testing "Will print an escape sequence"
    (is (= (with-out-str (print-ansi :clear))
           (str ESC "[2J")))))

(deftest clear-screen-test
  (testing "Will print a clear-screen sequence"
    (is (= (with-out-str (clear-screen))
           (str ESC "[2J")))))

(deftest reset-cursor-test
  (testing "Will print a reset-cursor sequence"
    (is (= (with-out-str (reset-cursor))
           (str ESC "[;H")))))

(deftest move-cursor-test
  (testing "Will print a move-cursor sequence"
    (is (= (with-out-str (move-cursor 2 4))
           (str ESC "[4;2;H")))))

(defn -main
  []
  (run-tests 'ansi-test))
