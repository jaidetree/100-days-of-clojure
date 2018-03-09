(ns animate
  (:require [clojure.core.async :as async]
            [ansi]
            [terminal]))

(def max-width (:columns (terminal/get-size)))
(def pos-chan (async/chan 1))
(def speed 10)

(defn -main []
  (async/go-loop
    [i max-width]
    (async/<! (async/timeout speed))
    (async/>! pos-chan i)
    (if (> i 0)
      (recur (dec i))
      (async/close! pos-chan)))
  (async/<!! (async/go-loop
               []
               (when-let [c (async/<! pos-chan)]
                 (ansi/clear-screen)
                 (ansi/move-cursor c 1)
                 (print "*")
                 (flush)
                 (recur))))
  (ansi/show-cursor))
