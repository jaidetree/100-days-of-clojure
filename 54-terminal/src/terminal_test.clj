(ns terminal-test
  (:require [clojure.test :refer :all]
            [terminal :refer :all]))

(deftest get-terminal-size-test
  (testing "Returns map with {:rows n :columns n}"
    (let [size (get-terminal-size)]
      (is (map? size))
      (is (number? (:rows size)))
      (is (number? (:columns size))))))

(defn -main
  []
  (run-tests 'terminal-test))
