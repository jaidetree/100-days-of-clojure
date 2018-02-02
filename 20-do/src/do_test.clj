(ns do-test)

(def data (range 1 4))

(defn -main []
  (println "doall: ")
  ;; Do all reads every item into memory and returns
  ;; the head of the list
  (println (= (doall (map (comp println str) data))
              '(nil nil nil)))
  ;; => 1
  ;; => 2
  ;; => 3
  ;; => true
  (println "doseq: ")
  ;; Returns nil and does not retain head of sequence
  (println (= (doseq [item data]
               (println item))
              nil))
  ;; => 1
  ;; => 2
  ;; => 3
  ;; => true
  (println "dorun: ")
  ;; Reads each item in the data and returns nil
  (println (= (dorun (map println data))
              nil)))
  ;; => 1
  ;; => 2
  ;; => 3
  ;; => true
