(ns formstack
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

;; Create a series of sequential formstack templates grouped with newlines.

(defn concat-after
  "Concats the end list to the beginning of a list.
   Returns a sequence"
  [end begin]
  (concat begin end))

(defn append
  "Creates a list with value conjoined to the end.
   Returns a sequence"
  [value coll]
  (concat coll [value]))

(defn append-when
  "Appends value to end of list when condition is true.
   Returns a sequence."
  [condition? value coll]
  (if condition?
    (concat [value] coll)
    coll))

(defn repeat-for
  "Repeatedly calls a function with the given args and an incrementing index
   Returns a sequence."
  [from to repeat-fn & args]
  (->> (range from (inc to))
       (mapcat #(apply repeat-fn (concat args [%])))
       (append "")))

(defn render
  "Takes a list of args and a list of templates and applies the args to each template.
   Returns a string"
  [args templates]
  (->> templates
       (map #(apply format (concat [%] args)))))

(defn create-space-photo
  "Creates a space photo template for the given space index and photo index.
   Returns a sequence"
  [space-index photo-index]
  (->> ["space-%d-photo-%d"]
       (render [space-index photo-index])))

(defn create-space-rule
  "Creates a series of space rule templates for the given space index and photo index.
   Returns a sequence."
  [space-index rule-index]
  (->> ["space-%d-space-rule-%d-name"
        "space-%d-space-rule-%d-start-time"
        "space-%d-space-rule-%d-start-time-ampm"
        "space-%d-space-rule-%d-end-time"
        "space-%d-space-rule-%d-end-time-ampm"
        "space-%d-space-rule-%d-days-of-week"
        "space-%d-space-rule-%d-rate"
        "space-%d-space-rule-%d-rate-rule"
        "space-%d-space-rule-%d-min-duration"
        "space-%d-space-rule-%d-min-subtotal-per-person"
        ""]
       (append-when (> rule-index 1) "space-%d-space-rule-%d-exists")
       (render [space-index rule-index])))

(defn create-space
  "Creates a space template with photo & space rule templates.
   Returns a list of templates."
  [space-index]
  (->> ["space-%d-name"
        "space-%d-semi-private"
        "space-%d-max-party"
        "space-%d-seated"
        ""]
       (append-when (> space-index 1) "space-%d-exists")
       (render [space-index])
       (concat-after (repeat-for 1 4 create-space-photo space-index))
       (concat-after (repeat-for 1 2 create-space-rule space-index))))

(defn -main
  "Takes a from and to string args and outputs space formstack templates"
  [from-str to-str]
  (let [from (Integer. from-str)
        to (Integer. to-str)]
    (->> (repeat-for from to create-space)
         (drop-last 4)
         (map println)
         (doall))))
