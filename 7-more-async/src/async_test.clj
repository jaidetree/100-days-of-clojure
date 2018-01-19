(ns async-test
  (:require [clojure.core.async :as async]))

;; DOESN'T WORK - Not sure why
(defn -main
  []
  (let [c (async/chan 10 (map identity))]
    (as-> (range 10) $
          (async/onto-chan c $)
          (async/<!! $)
          (println $))))
