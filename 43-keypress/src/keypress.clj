(ns keypress
  (:require [clojure.core.async :as async])
  (:import jline.Terminal))

(def keypress (async/chan 1))

(defn prompt []
  (println "Awaiting char...\n")
  (async/go-loop []
    (when-let [char (.readCharacter (Terminal/getTerminal) System/in)]
      (async/>! keypress char)
      (recur))))

(defn -main [& args]
  (prompt)
  (async/<!! (async/go-loop [char (async/<! keypress)]
               (println (str "Key pressed: " char))
               (recur (async/<! keypress)))))
