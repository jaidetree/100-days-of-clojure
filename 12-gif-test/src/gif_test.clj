(ns gif-test
  (:require [gif :as gif]
            [clojure.pprint :refer [pprint]]
            [clojure.core.async :as async]))

(defn -main
  [filename]
  (as-> (gif/read-gif-file filename) $
        (async/pipe $ (async/chan 1 (comp (mapcat gif/get-frames)
                                          (map gif/frame->image))))
        (async/go-loop []
          (when-let [frame (async/<! $)]
            (pprint frame)
            (recur)))
        (async/<!! $)))
