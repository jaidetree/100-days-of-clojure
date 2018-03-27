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
  [project]
  (pprint project)
  (let [id (:id project)]
    (:body
     (client/get
       (format "https://api.clubhouse.io/api/v2/projects/%s/stories?token=%s"
               id
               (:clubhouse_api_token config))
       {:as :json}))))

(defn fetch-clubhouse-projects
  []
  (:body
   (client/get
     (format "https://api.clubhouse.io/api/v2/projects?token=%s"
             (:clubhouse_api_token config))
     {:as :json})))

(defn parse-workflow-states
  [workflows]
  (->> workflows
        (take 1)
        (mapcat :states)
        (map #(hash-map (:name %) (:id %)))))

(defn -main
  []
  (pprint
   (->> (fetch-clubhouse-projects)
        (mapcat fetch-clubhouse-stories))))

(-main)
