(ns base64
  (:require [clojure.data.codec.base64 :as b64]
            [clojure.string :as str]))

(defn encode
  [s]
  (String. (b64/encode (.getBytes s)) "UTF-8"))

(defn -main []
  (as-> (line-seq (java.io.BufferedReader. *in*)) $
        (str/join "\n" $)
        (encode $)
        (println $)))
