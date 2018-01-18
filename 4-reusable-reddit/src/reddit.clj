(ns reddit
  (:require [clj-http.client :as client]))
              ;[reddit :refer [fetch-reddit-posts]]))

(defn fetch-json
  "Fetch url and return json map"
  [url]
  (client/get url {:as :json}))

(defn display-posts
  "Display reddit posts in a screen-friendly format"
  [posts]
  (doseq [post posts]
    (let [entry (:data post)]
      (println (:title entry))
      (println (str "  https://reddit.com" (:permalink entry)))
      (println (str "  " (:score entry) " | " (:num_comments entry) " comments"))
      (println "\n"))))

(defn get-reddit-posts
  "Fetch reddit posts"
  [query]
  (->> (str "https://www.reddit.com/r/clojure.json?" query)
       fetch-json
       :body
       :data
       :children))

(defn -main
  []
  (->> (get-reddit-posts "limit=5")
       display-posts))
