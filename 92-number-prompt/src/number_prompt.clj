(ns number-prompt)

(defn number-prompt-error
  []
  (println (str "Could not understand. Please put a whole number 1 – 10")))

(defn number-prompt
  [prompt-text]
  (println prompt-text)
  (let [answer (do (print "> ") (flush) (read-line))]
    (try
      (let [num (Integer. answer)]
        (if (or (< num 0) (> num 10))
          (do (number-prompt-error) #(number-prompt prompt-text))
          num))
      (catch Exception e
        (number-prompt-error)
        #(number-prompt prompt-text)))))

(defn -main
  []
  (println (trampoline number-prompt "Enter a number between 1 and 10:")))
