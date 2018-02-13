(ns print-emojis)

(def emojis
  {:cookie 0x1f36a
   :donut 0x1f369
   :burger 0x1f354
   :good 0x1f604
   :pizza 0x1f355
   :ok 0x1f612
   :bad 0x1f62d
   :sleepy 0x1f62a
   :cat 0x1f63a
   :sleepy-cat 0x1f431
   :angry-cat 0x1f63e
   :sleep 0x1f634
   :coffee 0x2615})

(defn emoji
  "Renders a keyword into a unicode emoji char"
  [name]
  (String. (Character/toChars (name emojis))))

(defn -main []
  (doseq [code (keys emojis)]
    (print (str (emoji code) " ")))
  (print "\n"))
