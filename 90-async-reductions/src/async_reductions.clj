(ns async-reductions
  (:require [clojure.core.async :as async]))

(defn ch-reductions
  [f init in-ch]
  (let [out-ch (async/chan)]
    (async/go
      (async/>! out-ch init)
      (loop [acc init]
        (when-some [x (async/<! in-ch)]
          (let [acc (f acc x)]
            (async/>! out-ch acc)
            (recur acc))))
      (async/close! out-ch))
    out-ch))

(defn -main
  []
  (as-> (ch-reductions + 0 (async/to-chan (range 5))) $
        (async/<!! (async/go-loop []
                                  (when-some [x (async/<! $)]
                                    (println x)
                                    (recur))))))
