(ns terminal-test
  (:require [clojure.test :refer :all]
            [terminal :refer :all]))

(deftest get-size-test
  (testing "Returns map with {:rows n :columns n}"
    (let [size (get-size)]
      (is (map? size))
      (is (number? (:rows size)))
      (is (number? (:columns size))))))

(deftest parse-size-test
  (testing "Returns map with {:rows n :columns n}"
    (let [size (parse-size "24 rows; 32 columns;")]
      (is (map? size))
      (is (= (:rows size) 24))
      (is (= (:columns size) 32)))))

(defn -main
  []
  (run-tests 'terminal-test))
