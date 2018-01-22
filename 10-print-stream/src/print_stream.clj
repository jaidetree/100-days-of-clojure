(ns print-stream
  (:require [reddit :refer [get-reddit-posts]]
            [clojure.core.async :as async]
            [clojure.pprint :refer [pprint pp]]))

(defn sink
  "Takes a side-effect function and a channel and calls the side effect on
  each item in the channel."
  [each-fn channel]
  (async/go-loop []
    (when-let [item (async/<! channel)]
      (each-fn item)
      (recur))))

(defn by-author
  [name]
  (fn [post] (= (:author post) name)))

(defn pipe-transducer
  [xform ch]
  (async/pipe ch (async/chan 1 xform)))

(def parse-posts
  (comp (map :data)
        (filter (by-author "eccentric_j"))))

(defn -main
  []
  (->> (get-reddit-posts "")
       (async/to-chan)
       (pipe-transducer parse-posts)
       (async/take 1)
       (sink pprint)
       (async/<!!)))
