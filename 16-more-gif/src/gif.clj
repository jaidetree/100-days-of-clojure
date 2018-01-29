(ns gif
  (:require [clojure.core.async :as async]
            [clojure.pprint :refer [pprint pp]])
  (:import (javax.imageio ImageIO ImageReader)
           (javax.imageio.metadata IIOMetadata)
           (javax.imageio.stream ImageInputStream)
           (java.awt Graphics)
           (java.awt.image BufferedImage)
           (java.net URL)
           (java.io IOException)
           (java.util Collections HashMap Map)
           (org.w3c.dom NamedNodeMap Node NodeList)))


(defn -nodelist->seq
  "Takes a w3c nodelist and returns a sequence of nodes"
  [nodelist]
  (map #(.item nodelist %)
    (take (.getLength nodelist)
      (iterate inc 0))))

(defn -node->map
  "Takes a node and returns a map {:x int :y int } from a w3c node"
  [target-attrs attrs-nodelist]
  (->> target-attrs
       (map (fn [[key prop-name]]
              [key (.getNamedItem attrs-nodelist prop-name)]))
       (filter (fn [[key attr-node]]
                (identity attr-node)))
       (reduce (fn [attrs [key attr-node]]
                (assoc attrs key (Integer/valueOf (.getNodeValue attr-node))))
        {})))

(defn -parse-meta
  "Takes imageMetadata and returns a map of {:x :y} "
  [meta]
  (->> (.getAsTree meta "javax_imageio_gif_image_1.0")
       (.getChildNodes)
       (-nodelist->seq)
       (filter #(= (.getNodeName %) "ImageDescriptor"))
       (map #(.getAttributes %))
       (first)
       (-node->map {:x "imageLeftPosition"
                    :y "imageTopPosition"})))

;; - [ ] TODO: Maybe this function should raise an error on failure?
(defn get-gif-from-file
  "Creates a Java IO File or outputs a warning and returns nil"
  [file-path]
  (let [file (clojure.java.io/file file-path)]
    (if (.exists file)
      file
      (do
        (println (format "File %s does not exist" file-path))
        nil))))

(defn create-reader
  "Takes a Java File and returns a channel that emits a gif ImageIO reader"
  [file]
  (let [reader-chan (async/chan 1)]
    (async/go
      (let [reader (.next (ImageIO/getImageReadersByFormatName "gif"))
            ciis (ImageIO/createImageInputStream file)]
        (.setInput reader ciis false)
        (async/>! reader-chan reader)))
    reader-chan))

;; - [X] TODO: Create a new buffered image
;;             new BufferedImage(
;;               image.getWidth(),
;;               image.getHeight(),
;;               BufferedImage.TYPE_INT_ARGB
;;             )
(defn frame->image
  "Takes a frame {:index :frame :meta} and returns a new BufferedImage"
  [{:keys [index frame meta]}]
  (let [image (BufferedImage.
                (.getWidth frame)
                (.getHeight frame)
                (BufferedImage/TYPE_INT_ARGB))
        graphics (.getGraphics image)]
    (.drawImage graphics image (:x meta) (:y meta) nil)
    image))

(defn read-frames
  "Takes a gif image reader channel and returns a channel of {:index :frame :meta} for
  each frame in a gif"
  [reader-channel]
  (let [frames-channel (async/chan)]
    (async/go
      (let [reader (async/<! reader-channel)
            total-frames (.getNumImages reader true)]
        (as-> (iterate inc 0) $
              (take total-frames $)
              (map (fn [index] {:index index
                                :frame (.read reader index)
                                :meta (-parse-meta (.getImageMetadata reader index))})
                   $)
              (doseq [frame $]
                (async/>! frames-channel frame)))
        (async/close! frames-channel)))
    frames-channel))

;; - [ ] TODO: Create a function that ties all the above functions together
