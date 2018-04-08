(ns group-range)

;; I know this is not a lot but after attempting to write a lazy scan reducer
;; it appears that may be the wrong approach to take and I think a better
;; means to achieve the goal of a animated floor generator for a game can be
;; done with partition... so here we are.

(defn -main
  []
  (->> (range)
       (partition 10)
       (take 3)
       (map println)
       (dorun)))
