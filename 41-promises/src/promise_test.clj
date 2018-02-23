(ns promise-test)

(defn -main
  []
  (let [p (promise)]
    (Thread/sleep 1000)
    (future (println @p))
    (deliver p "hello world")))
