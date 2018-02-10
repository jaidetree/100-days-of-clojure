(ns atoms-test
  (:require [clojure.pprint :refer [pprint]]))

(defn -main
  []
  (def mem (atom {}))
  (add-watch mem :watcher (fn [key atom old-state new-state]
                            (println (format "mem is now: %s" new-state))))

  (println @mem)

  (swap! mem assoc :key "value")

  (println @mem)
  (println (:key @mem))
  (println (= (:key @mem) (:key (deref mem)))))
