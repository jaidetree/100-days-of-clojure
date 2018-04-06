(ns middleware)

;; Project Goal: Write a middleware implementation similar to Node JS's
;;               Express webserver middleware)
;; UPDATE: 4/3/2018 - Received great feedback and better alternatives
;;         https://www.reddit.com/r/Clojure/comments/88xbx4/implementing_node_js_express_middleware_in/)
;; UPDATE: 4/5/2018 - Setup middleware through comp to retain expected order

(defn handler
  [[req res]]
  [req res])

(defn init
  "Simple init middleware to inform us that the party has started."
  [handler]
  (println "init")
  (fn init-handler [[req res]]
    (println "Middleware init")
    (Thread/sleep 1000)
    (handler [req (assoc res :status :init)])))

(defn update-1
  "Simple middleware to start updating our response atom."
  [handler]
  (println "update-1")
  (fn handler-1 [[req res]]
    (println "First middleware is running")
    (handler [req (assoc res :value 0)])))

(defn update-2
  "Simple middleware to further update our response atom"
  [handler]
  (println "update-2")
  (fn handler-2 [[req res]]
    (handler [req (update res :value inc)])))

(defn update-3
  "Slightly more complex middleware that waits for all other middleware to finish"
  [handler]
  (println "update-3")
  (fn handler-3 [[req res]]
    (let [res (update res :value inc)]
      (println (str "Current response value is: " res ". Calling next."))
      (let [[req res] (handler [req res])]
        (println (str "Response near end is: " res))
        [req (update res :value dec)]))))

(defn update-4
  "Middleware to reset the value to 1"
  [handler]
  (println "update-4")
  (fn handler-4 [[req res]]
    (let [res (assoc res :value 1)]
      (handler [req res]))))

(defn update-5
  "Final middleware to stop the chain."
  [handler]
  (println "update-5")
  (fn handler-5 [[req res]]
    [req (assoc res :done true)]))

(defn update-6
  "Middleware that should never run"
  [handler]
  (println "update-6")
  (fn handler-6 [[req res]]
    (println "Running middleware 6")
    (throw (Exception. "Eeek I was not supposed to run!"))))

; (def middleware (-> handler
;                     update-6
;                     update-5
;                     update-4
;                     update-3
;                     update-2
;                     update-1))
(def middleware ((comp
                    init
                    update-1
                    update-2
                    update-3
                    update-4
                    update-5
                    update-6) handler))



(defn -main
  []
  (println "Running middleware")
  (println (middleware [{:method :GET} {:done false}])))

(-main)
