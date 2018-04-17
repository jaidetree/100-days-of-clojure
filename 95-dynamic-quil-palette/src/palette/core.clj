(ns palette.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  ; Set frame rate to 1 frames per second.
  (q/frame-rate 1)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb))

(defn draw []
  (q/no-loop)
  (q/background 10)
  (doseq [y (range 130 371 240)]
    (doseq [x (range 130 371 240)]
      (q/fill (rand-int 256) 200 (+ (rand-int 156) 100))
      (q/ellipse x y 210 210))))


(defn -main [& args]
  (q/defsketch palette
    :title "The Sleepycat Palette"
    :size [500 500]
    :setup setup
    :draw draw))
