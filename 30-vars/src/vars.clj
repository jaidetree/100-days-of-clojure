(ns vars)

(def ^:dynamic *counter* 0)

(defn -main []
  ;; not entirely sure why it has to be binded into a local context.
  (binding [*counter* *counter*]
    (println (format "Initial: %s" *counter*))
    (set! *counter* 1)
    (println (format "After: %s" *counter*))))
