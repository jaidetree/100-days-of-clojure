(ns middleware)

;; Project Goal: Write a middleware implementation similar to Node JS's
;;               Express webserver middleware)
;; UPDATE: 4/4/2018 - Received great feedback and better alternatives
;;         https://www.reddit.com/r/Clojure/comments/88xbx4/implementing_node_js_express_middleware_in/)

(defn handler
  [[req res]]
  [req res])

(defn init
  "Simple init middleware to inform us that the party has started."
  [handler]
  (fn [[req res]]
    (println "Middleware init")
    (Thread/sleep 1000)
    (handler [req (assoc res :status :init)])))

(defn update-1
  "Simple middleware to start updating our response atom."
  [handler]
  (fn [[req res]]
    (println "First middleware is running")
    (handler [req (assoc res :value 0)])))

(defn update-2
  "Simple middleware to further update our response atom"
  [handler]
  (fn [[req res]]
    (handler [req (update res :value inc)])))

(defn update-3
  "Slightly more complex middleware that waits for all other middleware to finish"
  [handler]
  (fn [[req res]]
    (let [res (update res :value inc)]
      (println (str "Current response value is: " res ". Calling next."))
      (let [[req res] (handler [req res])]
        (println (str "Response near end is: " res))
        [req (update res :value dec)]))))

(defn update-4
  "Middleware to reset the value to 1"
  [handler]
  (fn [[req res]]
    (let [res (assoc res :value 1)]
      (handler [req res]))))

(defn update-5
  "Final middleware to stop the chain."
  [handler]
  (fn [[req res]]
    [req (assoc res :done true)]))

(defn update-6
  "Middleware that should never run"
  [handler]
  (fn [[req res]]
    (println "Running middleware 6")
    (throw (Exception. "Eeek I was not supposed to run!"))))

(def middleware (-> handler
                    update-6
                    update-5
                    update-4
                    update-3
                    update-2
                    update-1))

(defn -main
  []
  (println "Running middleware")
  (println (middleware [{:method :GET} {:done false}])))
