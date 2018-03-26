(ns clubhouse.core
  (:require [clj-http.client :as client]
            [clojure.pprint :refer :all]))

(def config (read-string (slurp "dev.secret.edn")))

(defn fetch-clubhouse-workflows
  []
  (:body
   (client/get
    (format "https://api.clubhouse.io/api/v2/workflows?token=%s"
            (:clubhouse_api_token config))
    {:as :json})))

(defn fetch-clubhouse-stories
  []
  (:body
   (client/request
    {:headers {}
     :method "GET"
     :url (format "https://api.clubhouse.io/api/v2/search/stories?token=%s"
                  (:clubhouse_api_token config))
     :accepts :json
     :content-type :json
     :as :json
     :body "{\"page_size\": 3, \"query\": \"owner:jayzawrotny state:500008928\"}"})))

(defn parse-workflow-states
  [workflows]
  (->> workflows
        (take 1)
        (mapcat :states)
        (map #(hash-map (:name %) (:id %)))))


(defn -main
  []
  (pprint
   (->> (fetch-clubhouse-stories)
        :data
        (map :name))))

(-main)
