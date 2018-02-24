(ns keypress
  (:import jline.Terminal))

(def keypress
  (future (.readCharacter (Terminal/getTerminal) System/in)))

(defn prompt []
  (println "Awaiting char...\n")
  (Thread/sleep 2000)
  (if-not (realized? keypress)
    (recur)
    (println "key: " @keypress)))

(defn -main [& args]
  (prompt)
  (shutdown-agents))
