(ns husky-please
  (:require [clj-http.client :as client]))

(defn get-config!
  "Read dev.secret.edn to get secret api keys
   or any other sensitive config.
   Returns map."
  [filename]
  (read-string (slurp filename)))

(defn get-giphy-url
  "Build giphy request url.
   Accepts a config map.
   Returns a url string."
  [{:keys [api_key]}]
  (format "http://api.giphy.com/v1/gifs/random?tag=husky&api_key=%s&rating=r"
    api_key))

(defn -main []
  "Prints a random husky url"
  (let [config (get-config! "dev.secret.edn")
        giphy-url (get-giphy-url config)
        response (client/get giphy-url {:as :json})]
    (println
     (get-in response [:body :data :image_original_url]))))
