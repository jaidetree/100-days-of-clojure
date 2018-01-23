(ns gif.core
  (:require [clojure.core.async :as async]
            [clojure.pprint :refer [pprint pp]])
  (:import (javax.imageio ImageIO ImageReader)
           (javax.imageio.metadata IIOMetadata)
           (javax.imageio.stream ImageInputStream)
           (java.awt Graphics)
           (java.awt.image BufferedImage)
           (java.net URL)
           (java.io File IOException)
           (java.util Collections HashMap Map)
           (org.w3c.dom NamedNodeMap Node NodeList)))

;; [ ] Return a channel that emits the reader
(defn read!
  "Takes a string pointing to a file path and returns a ImageIO reader"
  [file-path]
  (def reader (.next (.getImageReadersByFormatName ImageIO "gif")))
  (as-> (File. file-path)
        (.setInput reader $ false))
  (reader))

;; [ ] Create a new buffered image
;;     new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB)
(defn get-frames
  [reader]
  (let [total-frames (.getNumImages reader)
        frames (async/chan total-frames)]
    (async/go-loop [index 0]
      (when (< index total-frames)
       (async/>! #(.read reader index))
       (recur (inc index))))
    frames))
