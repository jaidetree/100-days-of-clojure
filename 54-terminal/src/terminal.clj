(ns terminal
  (:require [clojure.java.shell :as shell]))

(defn read-tty!
  "Returns the stdout of the stty -a command in current terminal"
  []
  (let [out (:out (shell/sh "/bin/sh" "-c" "stty -a < /dev/tty"))]
    (shutdown-agents)
    out))

(defn parse-size
  "Returns a map with {:rows n :columns n} of current terminal"
  [tty-str]
  (let [[s & dimensions] (re-find #"(\d+) rows; (\d+) columns;" tty-str)
        [rows columns] (map #(Integer. %) dimensions)]
    {:rows rows :columns columns}))

(defn get-size []
  (-> (read-tty!) (parse-size)))
