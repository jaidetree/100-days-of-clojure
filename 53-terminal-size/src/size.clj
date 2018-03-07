(ns size
  (:require [ansi :as ansi]
            [clojure.java.shell]))

(defn parse-terminal-size
  [[s rows columns]]
  {:rows rows
   :columns columns})

(defn get-terminal-size
  []
  (->> (clojure.java.shell/sh "/bin/sh" "-c" "stty -a < /dev/tty")
       :out
       (re-find #"(\d+) rows; (\d+) columns;")
       (parse-terminal-size)))

;; How do I determine the size of the terminal?
(defn -main
  []
  (println (get-terminal-size))
  (shutdown-agents))
