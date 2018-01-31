(ns mp4-test
  (:require [mp4 :refer [encode!]]
            [gif :as gif]
            [clojure.core.async :as async]))

(defn get-gif-frames
  [filename]
  (as-> (gif/get-gif-from-file filename) $
        (gif/create-reader $)
        (gif/read-frames $)
        (async/pipe $ (async/chan 1 (map gif/frame->image)))))


(defn create-mp4
  [filename frames]
  (encode! frames {:filename filename
                   :framerate 10}))

;; - [ ] TODO: Let's try writing each gif frame to a separate image file
;;             to make sure we're reading it right.
(defn -main
  [gif-src-file mp4-dest-file]
  (println gif-src-file)
  (println mp4-dest-file)
  (->> (get-gif-frames gif-src-file)
       (create-mp4 mp4-dest-file)
       (async/<!!)))
