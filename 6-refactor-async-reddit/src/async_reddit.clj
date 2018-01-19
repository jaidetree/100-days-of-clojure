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

(defn -main
  []
  (as-> (get-reddit-posts "limit=5") $
        (async/to-chan $)
        ;; replace with transduce
        (async/pipe $ (async/chan 1 (map :data)))
        (async/go-loop []
          (when-let [post (async/<! $)]
            (print-post post)
            (recur)))))

(-main)
