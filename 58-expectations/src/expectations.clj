(ns expectations
  "A error handling library that works within existing chainable constructs
   such as lazy lists, composition, transducers, and thread macros.")

(defn unexpected
  "Returns an either with a unexpected (unexpected) value"
  [arg & args]
  [:either arg :expected :exception])

(defn expected
  "Returns an either with an expected (expected) value."
  [arg & args]
  [:either :unexpected arg :exception])

(defn exception
  "Returns an either with an exception value."
  [arg]
  [:either :unexpected :expected arg])

(defn- is-exception?
  "Returns true if arg is an instance of Exception class"
  [arg]
  (instance? Exception arg))

(defn maybe
  "Returns an either with the excpected value if truthy or unexpected if falsey."
  [arg & args]
  (if (and arg (not (is-exception? arg)))
    (expected arg)
    (unexpected arg)))

(defn either?
  "Returns true if value is the :either keyword"
  [value]
  (= value :either))

(defn unexpected?
  [[type unexpected-value expected-value err]]
  (and (= expected-value :expected)
       (= err :exception)))

(defn expected?
  [[type unexpected-value expected-value err]]
  (and (= unexpected-value :unexpected)
       (= err :exception)))

(defn exception?
  [[type unexpected-value expected-value err]]
  (and (= unexpected-value :unexpected)
       (= expected-value :expected)))

(defn- forward
  "Calls a given function (f) against value and returns another either with
   an expected value if value is truthy."
  ([results]
   (if (either? results)
     results
     (maybe results)))
  ([f value]
   (try
     (let [results (f value)]
       (forward (f value)))
     (catch Exception e
       (exception e)))))

(defn- forward-using
  "Calls a given function (f) against a value and returns the specified either-type"
  ([type-fn results]
   (if (either? results)
     results
     (type-fn results)))
  ([type-fn f value]
   (try
     (forward-using type-fn (f value))
     (catch Exception e
       (exception e)))))

(defn to-unexpected
  "Transform the unexpected value in an [:either unexpected-value :expected]"
  ([f]
   (fn [results] (to-unexpected f results)))
  ([f results]
   (let [[type value] results]
    (if (unexpected? results)
      (forward-using unexpected f value)
      (forward-using unexpected results)))))

(defn on-unexpected
  "When the incoming either is unexpected use the given function to make it expected."
  ([f]
   (fn [results] (on-unexpected f results)))
  ([f results]
   (let [[type value] results]
    (if (unexpected? results)
      (forward f value)
      results))))

(defn on-expected
  "When the incoming either is expected use the given function against the
   expected value"
  ([f]
   (fn [results] (on-expected f results)))
  ([f results]
   (let [[type _ value] results]
    (if (expected? results)
      (forward f value)
      results))))

(defn when-unexpected
  "Run a predicate against the unexpected value, if return is truthy forward
   the return to an expected value."
  [p f]
  (fn [[type value :as args]]
    (if (and (unexpected? args) (p value))
      (forward f value)
      args)))

(defn when-expected
  "Run a predicate against the expected value, if return is truthy forward the
   expected value."
  [p f]
  (fn [[type unexpected value :as args]]
    (if (and (expected? args) (p value))
      (forward f value)
      args)))
