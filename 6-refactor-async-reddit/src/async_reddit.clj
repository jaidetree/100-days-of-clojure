(ns async-reddit
  (:require [reddit :refer [get-reddit-posts]]
            [clojure.core.async :as async]))

(defn get-post-link
  [post]
  (str "https://reddit.com" (:permalink post)))

(defn get-post-footer
  [post]
  (str (:score post) " | " (:num_comments post) " comments"))

(defn print-post
  [post]
  (doseq [display-fn [:title get-post-link get-post-footer]]
    (println (display-fn post)))
  (println "\n"))

;; While these are certainly reusable are they necessary?
(defn channel-for-each
  "Takes a side-effect function and a channel and calls the side effect on
  each item in the channel."
  [each-fn channel]
  (async/go-loop []
    (when-let [item (async/<! channel)]
      (each-fn item)
      (recur))))

(defn channel-map
  "Take a map-fn and a source channel and returns a channel with map-fn
  applied to each element of source channel"
  [map-fn from-channel]
  (async/pipe
    from-channel
    (async/chan 1 (map map-fn))))

(defn -main
  []
  (->> (get-reddit-posts "limit=5")
        (async/to-chan)
        (channel-map :data)
        (channel-for-each print-post)))
