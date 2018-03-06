(ns ansi)

(def ESC (char 27))

(def ansi-codes {:clear "[2J"
                 :reset "[;H"
                 :move "[%d;%d;H"})

(defn get-ansi-code
  [code]
  (if-let [format-str (code ansi-codes)]
    [:ok format-str]
    [:err (str "No ansi code found for " code)]))

(defn print-ansi
  "Takes a code keyword and any other args and prints an ANSI escape sequence"
  [code & args]
  (let [[status ansi-str] (get-ansi-code code)]
    (if (= status :ok)
      (print (str ESC (apply format ansi-str args)))
      (print (str "Error: " ansi-str)))))

(defn clear-screen
  "Prints the clear screen escape code"
  []
  (print-ansi :clear))

(defn reset-cursor
  "Prints the reset-cursor to top left position"
  []
  (print-ansi :reset))

(defn move-cursor
  "Moves the cursor to somewhere to a col, line on the screen"
  [x y]
  (print-ansi :move y x))
