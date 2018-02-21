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
  [y x]
  (print-ansi :move x y))

;; Displays a progress bar like
;; [---------------------]
;; ...until...
;; [=====================]
;; NOTE - Moving the cursor appears to also reset the line
(defn -main []
  (clear)
  (reset-cursor)
  ; (println "Hello World")
  ; (println "Hello World")
  ; (println "Hello World")
  ; (Thread/sleep 1000)
  ; (print (str (char 27) "[2J"))
  ; (clear)
  ; (Thread/sleep 1000)
  ; (reset-cursor)
  ; (Thread/sleep 1000)
  ; (println "I like cake")
  ; (Thread/sleep 1000)
  ; (Thread/sleep 1000)
  ; (move-cursor 25 25)
  (println "[------------------]")
  (doseq [x (range 2 21)]
    (move-cursor 1 1)
    (print "[")
    (print (apply str (repeat x "=")))
    (print (apply str (repeat (- 20 x) "-")))
    (print "]")
    (flush)
    (Thread/sleep 500)))
