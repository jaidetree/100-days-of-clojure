(ns conf
  (:require [clojure.java.io :as io]))

(defn load-config!
  "Loads an EDN file and returns a clojure EDN data structure
  Returns a vector [ :ok conf :status ] or [ :err \"message\" nil ]"
  ([filename] (load-config! filename nil))
  ([filename default]
   (if (.exists (io/file filename))
    (try
      (if-let [config (read-string (slurp filename))]
        [:ok config :read]
        [:ok default :default])
      (catch Exception e
        [:error (.getMessage e) nil]))

    (if (nil? default)
      [:error (str "EDN file " filename " does not exist.") nil]
      [:ok default :not-exists]))))
