(ns pidgeon-holer
  "A error handling library that works within existing chainable constructs
   such as lazy lists, composition, transducers, and thread macros.")

(defn unexpected
  "RetUrns an either with a unexpected (unexpected) value"
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
  (and (either? type)
       (= expected-value :expected)
       (= err :exception)))

(defn expected?
  [[type unexpected-value expected-value err]]
  (and (either? type)
       (= unexpected-value :unexpected)
       (= err :exception)))

(defn exception?
  [[type unexpected-value expected-value err]]
  (and (either? type)
       (= unexpected-value :unexpected)
       (= expected-value :expected)))

(defn- forward
  "Calls a given function (f) against value and returns another either with
   an expected value if value is truthy."
  ([results]
   (if (either? (first results))
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
   (if (either? (first results))
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
      results))))

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

(defn to-exception
  "When the incoming either is an exception apply the given function against
  the exception value.
  Returns another exception value."
  ([f]
   (fn [results] (to-exception f results)))
  ([f [_ _ _ value :as results]]
   (if (exception? results)
       (forward-using exception f value)
       results)))

(defn on-exception
  "When the incoming either is an exception apply the given function against
  the exception value.
  Returns expected either if function is applied."
  ([f]
   (fn [results] (on-exception f results)))
  ([f [_ _ _ value :as results]]
    (if (exception? results)
        (forward f value)
        results)))

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

(defn get-unexpected
  [[type value]]
  value)

(defn get-expected
  [[type _ value]]
  value)

(defn get-exception
  [[type _ _ value]]
  value)

(defn get-value
  [input]
  (cond (unexpected? input) (get-unexpected input)
        (expected? input) (get-expected input)
        (exception? input) (get-exception input)
        :else input))

(defn pipe
  ([f]
   (fn [input] (pipe f input)))
  ([f input]
   (if (either? (first input))
     (f (get-value input))
     input)))

(defn pipe-expected
  ([f]
   (fn [input] (pipe-expected f input)))
  ([f input]
   (if (expected? input)
     (f (get-expected input))
     input)))

(defn pipe-unexpected
  ([f]
   (fn [input] (pipe-unexpected f input)))
  ([f input]
   (if (unexpected? input)
     (f (get-unexpected input))
     input)))

(defn pipe-exception
  ([f]
   (fn [input] (pipe-exception f input)))
  ([f input]
   (if (exception? input)
     (f (get-exception input))
     input)))

(defn tap
  ([f]
   (fn [input] (tap f input)))
  ([f input]
   (if (either? (first input))
     (do
       (f (get-value input))
       input)
     input)))

(defn tap-expected
  ([f]
   (fn [input] (tap f input)))
  ([f input]
   (if (expected? input)
     (do
       (f (get-expected input))
       input)
     input)))


(defn tap-unexpected
  ([f]
   (fn [input] (tap f input)))
  ([f input]
   (if (unexpected? input)
     (do
       (f (get-unexpected input))
       input)
     input)))

(defn tap-exception
  ([f]
   (fn [input] (tap f input)))
  ([f input]
   (if (exception? input)
     (do
       (f (get-exception input))
       input)
     input)))
