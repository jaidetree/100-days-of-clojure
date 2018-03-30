(ns fn-util)

(defn tap
  "Takes a series of functions and applies them to a value. Assumed
   to be last in the list.
   Good for printing side-effects
   Returns original input value.
   "
  [& fns]
  (fn [x]
    (reduce #(%2 %1) x fns)
    x))
