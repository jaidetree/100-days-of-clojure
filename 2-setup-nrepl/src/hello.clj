(ns hello)

(defn -main
  []
  (println "Hello World!"))

(defn say-hi
  [suffix]
  (println (str "Hello " suffix "!"))
  suffix)

(say-hi "Pizza")
