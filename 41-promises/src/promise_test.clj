(ns promise-test)

(defn -main
  []
  (let [p (promise)]
    (future
     (println @p))
    (Thread/sleep 1000)
    (deliver p "hello world")
    (Thread/sleep 10)
    (System/exit 0)))
