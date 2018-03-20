(ns defmacro-test)

(defmacro tap
  [f]
  '(fn [x] ('f x) x))

(defn -main []
  (map (tap println) [1 2 3]))

(-main)
