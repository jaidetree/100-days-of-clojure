(ns dev.conf
  (:require [clojure.java.io :as io]))

(defn load-config!
  "Loads an EDN file and returns a clojure EDN data structure
  Returns a vector [ :ok conf :status ] or [ :err \"message\" nil ]"
  ([filename] (load-config! filename nil))
  ([filename default-value]
   (if (.exists (io/file filename))
    (try
      (if-let [config (read-string (slurp filename))]
        config
        default-value)
      (catch Exception e
        (if (nil? default-value)
          (throw (Exception. (str "EDN file " filename " could not be read.")))
          default-value))))))
