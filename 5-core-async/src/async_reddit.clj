(ns async-reddit
  (:require [reddit :refer [get-reddit-posts]]
            [clojure.core.async :as async]))

(defn posts->chan
  [posts]
  (async/to-chan posts))

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

(defn print-posts
  [posts-chan]
  (async/go-loop []
    (when-let [post (async/<! posts-chan)]
      (print-post post)
      (recur))))

(defn -main
  []
  (->> (get-reddit-posts "limit=5")
       (posts->chan)
       (print-posts)))

(-main)
