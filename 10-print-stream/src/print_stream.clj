(ns async-reddit
  (:require [reddit :refer [get-reddit-posts]]
            [clojure.core.async :as async]
            [clojure.pprint :refer [pprint pp]]))

(defn get-post-footer
  [post]
  (str (:score post) " | " (:num_comments post) " comments"))

(defn print-post
  [post]
  (doseq [display-fn [:title :url get-post-footer]]
    (println (display-fn post)))
  (println "\n"))

(defn sink
  "Takes a side-effect function and a channel and calls the side effect on
  each item in the channel."
  [each-fn channel]
  (async/go-loop []
    (when-let [item (async/<! channel)]
      (each-fn item)
      (recur))))

(defn -main
  []
  (as-> (get-reddit-posts "limit=1&author=eccentric_j") $
        (async/to-chan $)
        (async/pipe $ (async/chan 1 (comp (map :data))))
        (sink print-post $)))
