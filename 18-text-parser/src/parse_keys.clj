(ns parse-keys
  (:require [clojure.java.io :as io]))

(defn read-lines
  [filename]
  (let [rdr (io/reader filename)]
    (line-seq rdr)))

(defn tap [effect-fn]
  (fn [& args] (apply effect-fn args) (first args)))

; 4 spaces starting from 2
; 2 rules in each space

(defn -main
  [filename]
  (spit "keys.secret.txt" nil)
  (->> (read-lines filename)
       (filter #(re-find #"^space-" %))
       (map (tap println))
       (map #(spit "keys.secret.txt" (str % "\n") :append true))
       (doall)))
