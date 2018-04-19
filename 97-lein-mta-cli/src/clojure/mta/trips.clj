(ns mta.trips
  (:require [clj-http.client :as client]
            [cheshire.core :as json]
            [mta.stations :as mta-stations])
  (:import (com.google.transit.realtime GtfsRealtime$FeedMessage NyctSubway)
           (com.google.protobuf ExtensionRegistry)))

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

(defn stop->train
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

(defn trip->train
  [stations trip-update]
  (let [trip (.getTrip trip-update)
        stops (.getStopTimeUpdateList trip-update)]
    (map #(stop->train stations trip %) stops)))

(defn get-trips!
  [config stations]
  (as-> (client/get (get-url config) {:as :byte-array}) $
        (:body $)
        (bytestream->feed $)
        (.getEntityList $)
        (filter #(.hasTripUpdate %) $)
        (map #(.getTripUpdate %) $)
        (mapcat #(trip->train stations %) $)))
        ; (map #(.getTripUpdate %) $)
        ; (drop 2 $)
        ; (take 1 $)
        ; (print-table [:train :direction :station :station-id :arrives :line :latitude :longitude] $))))
        ; (doseq [item $]
        ;   (pprint item)))))
        ; (doseq [item $]
        ;   (println item)
        ;   (spit "data.secret.txt" item :append true)))))
