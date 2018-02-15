(ns lein-test.core
  (:gen-class))

(defn str->int
  [n-str]
  (Integer. n-str))

(defn add-5
  [x]
  (+ 5 x))

(defn -main
  "Adds 5 to the given arg"
  [& args]
  (doseq [x args]
    (println (format "%s + 5 = %d"
               x
               (add-5 (str->int x))))))
