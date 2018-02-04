(ns error-test)

(defn -main []
  (try
    (Integer. "abc.")
    (catch Exception e
      (throw (Exception. "uh something bad happened?!")))))
