(ns either)

(defn left
  "Returns an either with a left value"
  [arg & args]
  [:either arg :right :exception])

(defn right
  "Returns a right value with"
  [arg & args]
  [:either :left arg :exception])

(defn exception
  [arg]
  [:either :left :right arg])

(defn maybe
  [arg & args]
  (if (nil? arg)
    (right arg)
    (left arg)))

(defn err
  [arg & args]
  (left arg))

(defn exception?
  [arg]
  (instance? Exception arg))

(defn ok
  [arg & args]
  (if (exception? arg)
    (left arg)
    (right arg)))

(defn err?
  [type])
(defn either?
  [type]
  (= type :either))

(defn left?
  [[type left right e]]
  (and (= right :right)
       (= e :exception)))

(defn right?
  [[type left right e]]
  (and (= left :left)
       (= e :exception)))

(defn forward
  [f value]
  (try
    (let [results (f value)]
      (if (either? results)
        results
        (ok results)))
    (catch Exception e
      (exception e))))

(defn forward-using
  [type-fn f value]
  (try
    (let [results (f value)]
      (if (either? results)
        results
        (type-fn results)))
    (catch Exception e
      (exception e))))

(defn to-left
  ([f]
   (fn [results] (to-left f results)))
  ([f results]
   (let [[type value] results]
    (if (left? results)
      (forward-using left f value)
      results))))

(defn on-left
  ([f]
   (fn [results] (on-left f results)))
  ([f results]
   (let [[type value] results]
    (if (left? results)
      (forward f value)
      results))))

(defn to-right
  ([f]
   (fn [results] (to-right f results)))
  ([f results]
   (let [[type _ value] results]
    (if (right? results)
      (forward-using right f value)
      results))))

(defn on-right
  ([f]
   (fn [results] (on-right f results)))
  ([f results]
   (let [[type _ value] results]
    (if (right? results)
      (forward f value)
      results))))

(defn when-left
  [p f]
  (fn [[type value] {:as args}]
    (if (and (left? args) (p value))
      (forward f value)
      (args))))

(defn when-right [p f]
  (fn [[type _ value] {:as args}]
    (if (and (right? args) (p value))
      (forward f value)
      (args))))
