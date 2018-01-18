(ns async-reddit
  (:require [reddit :refer [get-reddit-posts]]
            [clojure.core.async :as async]))

(defn posts->chan
  [posts]
  (-> (async/to-chan posts)
      (async/pipe (async/chan 5 (map :data)))))

(defn get-post-link
  "Parse link from a reddit post"
  [post]
  (str "https://reddit.com" (:permalink post)))

(defn get-post-footer
  "Parse footer from a reddit post"
  [post]
  (str (:score post) " | " (:num_comments post) " comments"))

(defn print-post
  "Display a reddit post to console"
  [post]
  (doseq [display-fn [:title get-post-link get-post-footer]]
    (println (display-fn post)))
  (println "\n"))

(defn print-posts
  "Displays channel of reddit posts to console"
  [posts-chan]
  (async/go-loop []
    (when-let [post (async/<! posts-chan)]
      (print-post post)
      (recur))))

(defn -main
  "Get 5 of the latest reddit posts and print them to console"
  []
  (->> (get-reddit-posts "limit=5")
       (posts->chan)
       (print-posts)))
