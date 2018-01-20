(ns async-test
  (:require [clojure.core.async :as async]))

(defn -main
  []
  (let [c (async/chan 10 (map inc))]
    (as-> (range 10) $
          ;; onto-chan returns a separate channel from
          ;; the input one and works more like a side-effect
          (async/onto-chan c $)
          (async/go-loop []
            (when-some [v (async/<! c)]
              (println v)
              (recur))))))
