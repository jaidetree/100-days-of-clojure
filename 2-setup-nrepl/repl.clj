;; NOTE: It's probably better to use lein going forward.
(ns custom-repl
  ;; Repl requirements
  (:use [clojure.main :only (repl)]
         [clojure.tools.nrepl.server :only (start-server stop-server)]
         [cider.nrepl :only (cider-nrepl-handler)]
         [clojure.repl]
         [clojure.java.io :only (delete-file)]))

(defonce server (start-server :port 7888 :handler cider-nrepl-handler
  :greeting-fn (fn []
    (println "Welcome to nrepl"))))

(spit ".nrepl-port" "7888")

(repl
  :init (fn []
    (require 'hello)
    (in-ns 'hello)))
