(ns sort-sep
  (:require [clojure.string :as str]))

(defn -main
  "CLI tool to split a chunk of text by a given separator.
   Accepts a separator str and a list string separated by separator.
   Outputs an ordered, and deduped string list joined by separator.
   EXAMPLE:
   clj -m sort-sep , \"a,c,b\"
   ;; => \"a,b,c\""
  [sep list-str]
  (->> (str/split list-str (re-pattern sep))
       (sort)
       (dedupe)
       (str/join sep)
       (println)))
