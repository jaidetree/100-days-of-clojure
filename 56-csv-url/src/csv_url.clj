(ns csv-url
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [conf]))

(def config (second (conf/load-config! "dev.secret.edn")))

(defn read-file!
  [filename]
  (line-seq (io/reader filename)))

(defn -main [filename]
  (as-> (read-file! filename) $
        (drop 1 $)
        (map #(string/split % #",") $)
        (map #(get % 0) $)
        (map #(format (:url-template config) %) $)
        (doseq [url $]
          (println url))))
