(ns select
  (:require [dev.conf :refer [load-config!]]
            [clojure.pprint :refer :all]))

(def config (load-config! "dev.secret.edn"))

(defn help
  []
  (println "Usage: clj -m select <num> :eye-shadows"))

(defn -main
  ([]
   (help))
  ([n]
   (help))
  ([n key]
   (let [colors (get config (read-string key))
         number (Integer. n)
         total (count colors)]
     (->> (range 0 number)
          (map (fn [x] (rand-int total)))
          (map #(get colors %))
          (print-table))))
  ([n key & args]
   (help)))
