(ns gif-test
  (:require [gif :as gif]
            [clojure.pprint :refer [pprint]]
            [clojure.core.async :as async]))

;; How should this work?
;; - Read gif file returns a channel that will contain a gif reader
;; - Get frames returns a channel containing the frames of the gif
;; - frame->image takes a general frame object and creates a buffered image

(defn -main
  [filename]
  (as-> (gif/get-gif-from-file filename) $
        (gif/create-reader $)
        (gif/read-frames $)
        (async/pipe $ (async/chan 1 (map gif/frame->image)))
        (async/go-loop []
          (when-let [frame (async/<! $)]
            (pprint frame)
            (recur)))
        (async/<!! $)))
