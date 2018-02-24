(ns ansi
  (:require [clojure.string :as str]))

(def ansi-table {:clear "[2J"
                 :reset "[;H"
                 :move "[%d;%d;H"})

(defn print-ansi
  [code & args]
  (print (str (char 27) (apply format (get ansi-table code) args))))

(defn clear
  []
  (print-ansi :clear))

(defn reset-cursor
  []
  (print-ansi :reset))

(defn move-cursor
  [x y]
  (print-ansi :move y x))

(defn draw-box
  [x y width height]
  (move-cursor x y)
  (doseq [r (range 0 width)]
    (print "*"))
  (doseq [c (range (inc y) (+ y height))]
    (move-cursor x c)
    (print (str "*")))
  (move-cursor x (+ y height))
  (doseq [r (range 0 width)]
    (print "*"))
  (doseq [c (range (inc y) (+ y height))]
    (move-cursor (dec (+ x width)) c)
    (print (str "*"))))

 ;; Wanted to better learn if it's possible to layer on text in sections
 ;; instead of having to output everything as a full matrix
(defn -main []
  (clear)
  (move-cursor 10 5)
  (print "Hello World (left)")
  (move-cursor 10 6)
  (print "Hello World (left)")
  (move-cursor 10 7)
  (print "Hello World (left)")
  (flush)
  (print "\n")
  (move-cursor 60 5)
  (print "Hello World (right)")
  (move-cursor 60 6)
  (print "Hello World (right)")
  (move-cursor 60 7)
  (print "Hello World (right)")
  (print "\n\n\n")
  (reset-cursor)
  (draw-box 4 20 30 10)
  (draw-box 40 20 30 10)
  (flush)
  (move-cursor 17 25)
  (print "Left")
  (move-cursor 52 25)
  (print "Right")
  (flush)
  (move-cursor 16 25)
  (print "(")
  (move-cursor 21 25)
  (print ")")
  (move-cursor 51 25)
  (print "(")
  (move-cursor 57 25)
  (print ")")
  (move-cursor 0 120)
  (flush))
