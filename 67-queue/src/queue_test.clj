(ns queue-test
  (:require [clojure.test :refer :all]
            [clojure.core.async :as async]))

(def queue (atom []))
(def c (async/chan))
(def o (async/chan))
(def output (atom []))

(defn drain
  [f]
  (doseq [item @queue]
    (f item))
  (reset! queue []))

(deftest queue-print-test
  (async/go-loop []
    (when-let [item (async/<! c)]
      (swap! queue conj item)
      (recur)))
  (async/go-loop []
    (when-let [item (async/<! o)]
      (println (str "Received " item))
      (swap! output conj item)
      (recur)))
  (testing "Can queue items at any time"
    (async/>!! c 1)
    (async/>!! c 2)
    (async/>!! c 3)
    (is (= (count @queue) 3))
    (drain #(async/>!! o %))
    (is (= (count @queue) 0))
    (async/close! o)
    (async/close! c)
    (is (= @output [1 2 3]))))

(defn -main
  []
  (run-tests 'queue-test))

(-main)
