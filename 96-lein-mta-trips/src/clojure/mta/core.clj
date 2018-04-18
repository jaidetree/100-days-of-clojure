(ns mta.core
  (:require [clj-http.client :as client]
            [cheshire.core :as json]
            [mta.mta-stations]
            [clojure.pprint :refer [print-table]]
            [clojure.reflect :as r])
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

(defn calc-arrival
  [arrival]
  (let [now (quot (System/currentTimeMillis) 1000)]
    (int (/ (- arrival now) 60))))

(defn trip->train
  [stations train direction stop]
  (println (take 1 stations))
  (let [stop-id (.getStopId stop)
        station (mta-stations/find-station stop-id stations)
        arrives (calc-arrival (.getTime (.getArrival stop)))]
    {:train train
     :stop-id stop-id
     :direction direction
     :station station
     :arrives arrives}))

(defn parse-train
  [stations trip-update]
  (let [trip (.getTrip trip-update)
        train (.getRouteId trip)
        direction "NORTH"
        stops (.getStopTimeUpdateList trip-update)]
    ; (print-table (:members (r/reflect trip)))
    (print (.getDirectionId trip))
    (map #(trip->train stations train direction %) stops)))

(defn -main []
  (let [config (get-config "dev.secret.edn")
        stations (mta-stations/get-stations!)]
    (as-> (client/get (get-url config) {:as :byte-array}) $
          (:body $)
          (bytestream->feed $)
          (.getEntityList $)
          (filter #(.hasTripUpdate %) $)
          (map #(.getTripUpdate %) $)
          (mapcat #(parse-train stations %) $)
          ; (map #(.getTripUpdate %) $)
          ; (drop 2 $)
          (take 1 $)
          (doseq [item $]
            (println item)))))
          ; (doseq [item $]
          ;   (println item)
          ;   (spit "data.secret.txt" item :append true)))))

(-main)
