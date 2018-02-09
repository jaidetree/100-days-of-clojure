(ns gif
  (:require [clj-http.client :as client]
            [conf :refer [load-config!]]))

(def config (let [[status config] (load-config! "dev.secret.edn")]
              (if (= status :ok)
                config
                (throw (Exception. "Config file dev.secret.edn not found")))))

(defn get-giphy-url
  "Build giphy request url.
   Accepts a config map.
   Returns a url string."
  [{:keys [api_key]} tag]
  (format "http://api.giphy.com/v1/gifs/random?tag=%s&api_key=%s&rating=r"
    tag
    api_key))

(defn parse-gif
  [response]
  (get-in response [:body :data :image_original_url]))

(defn request-gif!
  [tag]
  (-> (get-giphy-url config tag)
      (client/get {:as :json})
      (parse-gif)))

(defn prompt-tag
  []
  (println "What would you like a gif of?")
  (print "> ")
  (flush)
  (let [tag (read-line)]
    (if (empty? tag)
      (do
        (println "Ok. Bye!")
        nil)
      (do
        (println (request-gif! tag))
        (println "")
        (prompt-tag)))))

(defn -main
  []
  (prompt-tag))
