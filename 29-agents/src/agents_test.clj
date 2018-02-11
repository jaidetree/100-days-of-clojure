(ns agents-test)

(defn -main []
  (def counter (agent 0))
  (add-watch counter :watcher (fn [key a old-state new-state]
                                (println (format "Updating counter: %s" new-state))))
  (println (format "Initial %s" @counter))
  ;; => 0
  (send counter inc)
  (println (format "After %s " @counter))
  ;; => 0
  ;; => "Updating counter: 1"
  (send-off counter inc)
  ;; => "Updating counter: 2"
  (println (format "Now %s %b" @counter (= @counter 2)))
  (Thread/sleep 10)
  (remove-watch counter :watcher)
  (System/exit 0))
