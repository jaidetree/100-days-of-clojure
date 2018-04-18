(ns mta.core
  (:require [clj-http.client :as client]
            [cheshire.core :as json]
            [mta.mta-stations]
            [clojure.pprint :refer [print-table pprint]]
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

(defn trip->direction
  [trip]
  (-> trip
      (.getExtension NyctSubway/nyctTripDescriptor)
      (.getDirection)
      (.toString)))

(defn trip->train
  [stations trip stop]
  (let [stop-id (.getStopId stop)
        station (mta-stations/find-station stop-id stations)
        arrives (calc-arrival (.getTime (.getArrival stop)))
        train (.getRouteId trip)
        direction (trip->direction trip)]
    {:train train
     :stop-id stop-id
     :direction direction
     :station-id (:stop-id station)
     :station (:name station)
     :line (:line station)
     :latitude (:latitude station)
     :longitude (:longitude station)
     :arrives arrives}))

(defn parse-train
  [stations trip-update]
  (let [trip (.getTrip trip-update)
        stops (.getStopTimeUpdateList trip-update)]
    (map #(trip->train stations trip %) stops)))

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
          ; (take 1 $)
          (print-table [:train :direction :station :station-id :arrives :line :latitude :longitude] $))))
          ; (doseq [item $]
          ;   (pprint item)))))
          ; (doseq [item $]
          ;   (println item)
          ;   (spit "data.secret.txt" item :append true)))))
