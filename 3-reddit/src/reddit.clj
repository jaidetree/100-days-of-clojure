(ns reddit
  (:require [clojure.core.async :as async]
            [clojure.pprint :refer [pprint]]
            [clojure.data.json :as json]
            [clj-http.client :as client]))

(defn parse-json
  "Parses json string input into keyword based EDN data"
  [data]
  (json/read-str data :key-fn keyword))

(defn display-post
  "Display a human readable entry format"
  [entry]
  (println (:title entry))
  (println (:permalink entry))
  (println (str "score: " (:score entry)
                " | comments: " (:num_comments entry)))
  (println ""))

(defn prefix-link
  "Update the permalink to contain full url"
  [entry]
  (update entry :permalink #(str "https://reddit.com" %1)))

(defn fetch-reddit-posts
  "Fetch last 5 reddit posts, parse, pluck our way into something useful"
  []
  (println "Fetching reddit posts in r/clojure...\n")
  (->> "https://www.reddit.com/r/clojure.json?limit=5"
       client/get))

(defn display-posts
  "Parse reddit posts and display each post"
  [response]
  (->> (:body response)
      parse-json
      :data
      :children
      (map (comp display-post prefix-link :data))))

(defn -main
  []
  (doall (display-posts (fetch-reddit-posts))))
