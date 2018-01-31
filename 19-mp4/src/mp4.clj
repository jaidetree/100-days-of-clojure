;; Using source from https://github.com/mikera/telegenic/blob/master/src/main/clojure/telegenic/core.clj
(ns mp4
  (:require [clojure.core.async :as async])
  (:import [java.io File])
  (:import [java.awt.image BufferedImage])
  (:import [org.jcodec.codecs.h264 H264Encoder]
           [org.jcodec.api SequenceEncoder]
           [org.jcodec.common.model Picture]
           [org.jcodec.scale AWTUtil]))

(defn encode!
  "Encodes a channel of frames to a video file.
   Returns a map decribing the encoding the result."
  ([frames]
   (encode! frames nil))
  ([frames options]
   (let [start-time (System/currentTimeMillis)
         filename (:filename options "out.mp4")
         ^File file (:file options (File. filename))
         framerate (:framerate options 30)
         enc (SequenceEncoder/createSequenceEncoder file (int framerate))
         counter (atom 0)
         out (async/chan 1)]
      (async/go-loop []
        (if-let [^BufferedImage frame (async/<! frames)]
          (do
            (println frame)
            (let [^Picture picture (AWTUtil/fromBufferedImageRGB frame)]
              (.encodeNativeFrame enc picture)
              (swap! counter inc))
            (recur))
          (do
            (.finish enc)
            (async/>! out {:filename filename
                           :frame-count @counter
                           :time (* 0.001 (- (System/currentTimeMillis) start-time))}))))
      out)))
