(ns middleware)

;; Project Goal: Write a middleware implementation similar to Node JS's
;;               Express webserver middleware)

(defn run-middleware
  "Threads the req & res atoms through each middleware fn.
  Returns response atom value."
  [req res middleware]
  (let [[middleware-fn & rest] middleware]
    (if (and middleware-fn (not (get @res :done false)))
      (middleware-fn req res (fn [] (run-middleware req res rest)))
      @res)))

(defn init
  "Simple init middleware to inform us that the party has started."
  [req res next]
  (println "Middleware init")
  (swap! res assoc :status :init)
  (Thread/sleep 1000)
  (next))

(defn update-1
  "Simple middleware to start updating our response atom."
  [req res next]
  (swap! res assoc :value 0)
  (next))

(defn update-2
  "Simple middleware to further update our response atom"
  [req res next]
  (swap! res update :value inc)
  (next))

(defn update-3
  "Slightly more complex middleware that waits for all other middleware to finish"
  [req res next]
  (swap! res update :value inc)
  (println (str "Current response value is: " @res ". Calling next."))
  (next)
  (println (str "Value near end is: " @res))
  (swap! res update :value dec))

(defn update-4
  "Middleware to reset the value to 1"
  [req res next]
  (swap! res assoc :value 1)
  (println (str "Reset value to: " @res))
  (next))

(defn update-5
  "Final middleware to stop the chain."
  [req res next]
  (swap! res assoc :done true)
  (next))

(defn update-6
  "Middleware that should never run"
  [req res next]
  (println "Running middleware 6")
  (throw (Exception. "Eeek I was not supposed to run!")))

(defn -main
  []
  (println "Running middleware")
  (println
   (trampoline run-middleware
               (atom {})
               (atom {})
               [init
                update-1
                update-2
                update-3
                update-4
                update-5
                update-6])))

(-main)
