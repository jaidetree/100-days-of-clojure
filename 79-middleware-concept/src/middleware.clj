(ns middleware)

;; Project Goal: Write a middleware implementation similar to Node JS's
;;               Express webserver middleware)

(defn run-middleware
  [req res middleware]
  (let [[middleware-fn & rest] middleware]
    (if middleware-fn
      (middleware-fn req res (fn [] (run-middleware req res rest)))
      @res)))

(defn init
  [req res next]
  (println "Middleware init")
  (swap! res assoc :status :init)
  (Thread/sleep 1000)
  (next))

(defn update-1
  [req res next]
  (swap! res assoc :value 0)
  (next))

(defn update-2
  [req res next]
  (swap! res update :value inc)
  (next))

(defn update-3
  [req res next]
  (swap! res update :value inc)
  (println (str "Calling next."))
  (next)
  (println (str "Value is: " @res))
  (swap! res update :value dec))

(defn update-4
  [req res next]
  (swap! res assoc :value 1)
  (println (str "Reset value to: " @res))
  (next))

(defn -main
  []
  (println "Running middleware")
  (println
   (trampoline run-middleware (atom {}) (atom {}) [init
                                                   update-1
                                                   update-2
                                                   update-3
                                                   update-4])))

(-main)
