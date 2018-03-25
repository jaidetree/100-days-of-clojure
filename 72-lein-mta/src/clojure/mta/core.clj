(ns mta.core
  (:require [clj-http.client :as client]
            [cheshire.core :as json])
  (:import (com.google.transit.realtime GtfsRealtime$FeedMessage NyctSubway)
           (com.google.protobuf ExtensionRegistry)))

(defn get-config
  [filename]
  (read-string (slurp filename)))

(defn create-registry
  "Register the NYCT Protobuf extension.
  Returns a registry instance."
  []
  (doto
    (ExtensionRegistry/newInstance)
    NyctSubway/registerAllExtensions))

(defn bytestream->feed
  [feed-stream]
  (->> (GtfsRealtime$FeedMessage/parseFrom feed-stream (create-registry))))

(defn get-url
  [config]
  (format "%s?key=%s&feed_id=16"
    (:url config)
    (:token config)))

(defn -main []
  (spit "full-data.secret.txt" "")
  (let [config (get-config "dev.secret.edn")]
    (as-> (client/get (get-url config) {:as :byte-array}) $
          (:body $)
          (bytestream->feed $)
          ; (filter #(.hasTripUpdate %) $)
          ; (map #(.getTripUpdate %) $)
          ; (drop 2 $)
          ; (take 1 $)
          (doto $ println)
          (spit "full-data.secret.txt" $ :append true))))
          ; (doseq [item $]
          ;   (println item)
          ;   (spit "data.secret.txt" item :append true)))))
