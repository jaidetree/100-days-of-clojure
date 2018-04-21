(ns webserver.core
  (:require [clojure.pprint :refer [pprint]]
            [clj-http.client :as client])
  (:gen-class))

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

(def config (get-config! "dev.secret.edn"))

(defn husky-please! []
  "Prints a random husky url"
  (let [giphy-url (get-giphy-url config)
        response (client/get giphy-url {:as :json})]
     (get-in response [:body :data :image_original_url])))

(defn handler
  [request]
  (let [husky-gif-url (husky-please!)]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (format
            "<!doctype html>
             <html>
              <head>
               <title>Husky Please</title>
              </head>
              <body>
                <img src=\"%s\" alt=\"Husky\" />
              </body>
            </html>"
            husky-gif-url)}))
