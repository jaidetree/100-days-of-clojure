(ns count
  (:require [clojure.string :as str]))

(defn count-placeholders
  "Accepts a template string like /page/{}/article/{}/{}-{}/
   Returns the number of placeholders {}"
  [s]
  (-> s
      (str/split #"\{\}")
      (count)))

(defn -main
  "Accepts a template string like /path/to/url/{}-{}/{}/comments/{} and
   outputs the number of {}"
  [s]
  (println (count-placeholders s)))
