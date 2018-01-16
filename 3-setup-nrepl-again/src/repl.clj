;; NOTE: I did not write most of this
;; https://github.com/mrroman/devtools/blob/master/src/devtools/nrepl.clj

(ns repl
  (:require [clojure.tools.nrepl.server :as nrepl-server]
            [clojure.java.io :as io]
            [cider.nrepl :refer [cider-nrepl-handler]]))

(defn create-nrepl-port-file
  [port]
  (let [nrepl-port-file (io/file ".nrepl-port")]
    (spit nrepl-port-file (str port))
    (.addShutdownHook (Runtime/getRuntime)
                      (Thread. #(io/delete-file nrepl-port-file true)))))
(defn -main []
  (let [{port :port}
        (nrepl-server/start-server :handler cider-nrepl-handler)]
    (println "nrepl server started at port" port)
    (create-nrepl-port-file port)))
