(ns deploy
  (:require [clj-http.client :as client]
            [conf :as conf]
            [clojure.string :as str]
            [clojure.pprint :refer [pp pprint]]
            :reload-all))

(def deploy-opts #{:migrate :copy :force :deploy :install})

(defn load-config
  "Accepts a relative filename and returns the edn file."
  [state filename]
  (second (conf/load-config! filename)))

(defn merge-using
  "Accepts a function that returns a map partial and args. Last arg is
   assumed to be state.
   Returns state merged with the result of the merge-fn merged in."
  [merge-fn & args]
  (let [state (last args)
        args (drop-last args)]
    (merge-with into state (apply merge-fn (concat [state] args)))))

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
  [state]
  {:url (str (:build-server state) (:build-url state))})

(defn get-target-branch
  "Accepts a map of current app state and branch string param.
   Returns map partial to be merged into state."
  [state branch]
  (if-let [target-branch (get (:servers state) (keyword branch))]
    {:params {:targetBranch target-branch}}
    (throw (Exception. (str "Branch " branch " is not a valid target.")))))

(defn format-request
  "Creates a deploy request to build server.
   Accepts...
     - State map of data
   Returns map to send server request with"
  [state]
  {:url (:url state)
   :options {:form-params (:params state)
             :content-type :json
             :as :json}})

(defn request-deploy
  "Parses arguments, reads config from private config file.
   Returns JSON POST response."
  [post-fn request]
  (post-fn (:url request) (:options request)))

(defn parse-response
  "Accepts a server response map and returns the response body JSON"
  [response]
  (:body response))

(defn -main
  "Posts a build & deploy request to a remote build server.
   Accepts a branch string and a list of param strings whitelisted in deploy-opts
   Prints JSON POST response."
  [branch & args]
  (->> args
       (parse-args)
       (assoc {} :params)
       (merge-using load-config "dev.secret.edn")
       (merge-using get-build-url)
       (merge-using get-target-branch branch)
       (format-request)
       (request-deploy client/post)
       (parse-response)
       (pprint)))
