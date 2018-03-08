(ns terminal
  (:require [clojure.java.shell :as shell]))

(defn read-tty!
  "Returns the stdout of the stty -a command in current terminal"
  []
  (->> (shell/sh "/bin/sh" "-c" "stty -a < /dev/tty")
       :out))

(defn get-terminal-size
  "Returns a map with {:rows n :columns n} of current terminal"
  []
  (let [tty-str (read-tty!)
        [s & dimensions] (re-find #"(\d+) rows; (\d+) columns;" tty-str)
        [rows columns] (map #(Integer. %) dimensions)]
    (shutdown-agents)
    {:rows rows :columns columns}))
