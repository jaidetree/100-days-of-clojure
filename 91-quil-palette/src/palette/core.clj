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
  (q/with-translation [110 110]
    (q/fill (rand-int 256) 200 255)
    (q/ellipse 20 20 210 210)
    (q/fill (rand-int 256) 200 255)
    (q/ellipse 260 20 210 210)
    (q/fill (rand-int 256) 200 255)
    (q/ellipse 20 260 210 210)
    (q/fill (rand-int 256) 200 255)
    (q/ellipse 260 260 210 210)))


(defn -main [& args]
  (q/defsketch palette
    :title "The Sleepycat Palette"
    :size [500 500]
    :setup setup
    :draw draw))
