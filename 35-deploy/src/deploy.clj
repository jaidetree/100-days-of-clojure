(ns deploy
  (:require [clj-http.client :as client]
            [conf :as conf]
            [clojure.string :as str]
            [clojure.pprint :refer [pp pprint]]
            :reload-all))

(def deploy-opts #{:migrate :copy :force :deploy :install})

(defn arg->keyword
  "Accepts an arg string like --migrate
   Returns keyword :migrate"
  [arg-str]
  (-> arg-str
      (str/replace #"--" "")
      (str/lower-case)
      (keyword)))

(defn parse-args
  "Accepts a list of arg strings like [--migrate --force]
   Returns a map of provided args {:migrate true :force true}"
  [args]
  (let [args (map arg->keyword args)]
    (->> args
         (filter #(contains? deploy-opts %))
         (map #(vector % true))
         (into {}))))

(defn get-build-url
  "Accepts a config map and returns the resulting URL string"
  [config]
  (str (:build-server config) (:build-url config)))

(defn get-target-branch
  [config branch]
  (if-let [target-branch (get (:servers config) (keyword branch))]
    target-branch
    (throw (Exception. (str "Branch " branch " is not a valid target.")))))

(defn create-deploy-request
  "Creates a deploy request to build server.
   Accepts...
     - clj-http-like post function
     - branch string like \"staging\",
     - map of params allowed in deploy-opts,
     - config map read from a private server config file
   Returns JSON response body.
   Example:
   (create-deploy-request \"staging\" {:migrate true} {:build-server \"localhost\"
                                                       :build-url \"/build\"})"
  [post-fn branch params config]
  (let [target-branch (get-target-branch config branch)]
    (->> {:form-params (assoc params :targetBranch target-branch)
          :content-type :json}
         (post-fn (get-build-url config))
         :body)))

(defn request-deploy
  "Parses arguments, reads config from private config file.
   Returns JSON POST response."
  [post-fn branch args]
  (create-deploy-request
    post-fn
    branch
    (parse-args args)
    (second (conf/load-config! "dev.secret.edn"))))

(defn -main
  "Posts a build & deploy request to a remote build server.
   Accepts a branch string and a list of param strings whitelisted in deploy-opts
   Prints JSON POST response."
  [branch & args]
  (pprint (request-deploy client/post branch args)))
