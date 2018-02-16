(ns test
  (:require [clojure.test :refer :all]
            [count :refer [count-placeholders]]))

(deftest count-test
  (testing "Can count placeholders in a template"
    (is (= (count-placeholders "/page/{}/article/{}/{}-{}") 4))))

(defn -main
  []
  (run-tests 'test))
