(ns more-lazy)

(def repeated-greeting (take 5
                        (repeatedly (constantly "hi"))))
(def repeat-greeting (take 5
                      (repeat "hi")))

(def cycle-greeting (take 5 (cycle ["hi" "hello" "whats up"])))
(def expected ["hi" "hi" "hi" "hi" "hi"])

(println (str "repeated-greeting === [hi hi hi hi hi]: " (= expected repeated-greeting)))
(println (str "repeated-greeting === '(hi hi hi hi hi): " (= '("hi" "hi" "hi" "hi" "hi") repeated-greeting)))
(println (str "repeat-greeting === [hi hi hi hi hi]: " (= expected repeat-greeting)))
(println (str "cycle-greeting === [hi hello whats up hi hello]: " (= ["hi" "hello" "whats up" "hi" "hello"] cycle-greeting)))
(println repeated-greeting)
(println repeat-greeting)
(println cycle-greeting)
