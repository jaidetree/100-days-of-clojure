(ns mta
  (:require [clj-http.client :as client]
            [cheshire.core :as json])
  (:import (com.google.transit.realtime GtfsRealtime$FeedMessage)))

(defn get-config
  [filename]
  (read-string (slurp filename)))

(defn str->feed
  [feed-str]
  (->> (GtfsRealtime$FeedMessage/parseFrom feed-str)
       (.getEntityList)))

(defn get-url
  [config]
  (format "%s?key=%s&feed_id=16"
    (:url config)
    (:token config)))

(defn -main []
  (spit "data.secret.txt" "")
  (let [config (get-config "dev.secret.edn")]
    (as-> (client/get (get-url config) {:as :byte-array}) $
          (:body $)
          (str->feed $)
          (filter #(.hasTripUpdate %) $)
          (map #(.getTripUpdate %) $)
          (doseq [item $]
            (println item)
            (spit "data.secret.txt" item :append true)))))

(-main)
