(ns spec-test
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure.spec.gen.alpha :as gen]
            [clojure.pprint :refer [pprint]]))

;; https://clojure.org/guides/spec

(s/def ::even even?)

(def email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")
(def username-regex #"[_a-z0-9]+")
(s/def ::email-type (s/and string? #(re-matches email-regex %)))

(s/def ::id int?)
(s/def ::name string?)
(s/def ::email ::email-type)
(s/def ::username (s/and string? #(re-matches username-regex %)))

(s/def ::person (s/keys :req [::id ::name ::email]
                        :opt [::username]))


(defn add
  [x y]
  {:pre [(s/valid? int? x) (s/valid? int? y)]
   :post [(s/valid? #(>= % 10) %)]}
  (+ x y))

(defn mult
  [x y]
  (* x y))

(s/fdef mult
  :args (s/and (s/cat :x int? :y int?)
               #(< (:x %) (:y %)))
  :ret int?
  :fn (s/and #(> (:ret %) (-> % :args :x))
             #(> (:ret %) (-> % :args :y))))

(defn -main
  []
  (println (s/conform even? 1000))
  (println (s/conform even? 1001))
  (println (s/valid? even? 100))
  (s/explain ::even 1001)
  (s/explain ::person
    {::id 2000
     ::name "Dab Dusky"
     ::email "dab@dusky.net"
     ::username "dab_dusky_55"})
  (println (add 8 2))
  (println (mult 2 4))
  ; (println (mult 0 4)))
  (pprint (s/exercise-fn `mult)))
  ; (println (mult "5" 4)))
  ; (println (add 8.4 2))
  ; (println (add 8 1)))

(stest/instrument `mult)
(-main)
(pprint (stest/abbrev-result (first (stest/check `mult))))
