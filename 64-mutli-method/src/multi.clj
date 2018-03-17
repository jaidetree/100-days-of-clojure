(ns multi
  (:require [pidgeon-holer :refer :all]))

(defn get-either-type
  [input]
  (cond (vector? input) :vector
        (map? input) :map
        :else nil))

(defmulti get-any-value get-either-type)
(defmethod get-any-value :vector [input]
  (cond (unexpected? input) (get-unexpected input)
        (expected? input) (get-expected input)
        (exception? input) (get-exception input)
        :else input))

(defmethod get-any-value :map [input]
  (->> [:unexpected :expected :exception identity]
       (map #(% input))
       (filter identity)
       (first)))

(defmethod get-any-value :default [input]
  (unexpected input))
