(ns pidgeon-holer
  "A error handling library that works within existing chainable constructs
   such as lazy lists, composition, transducers, and thread macros.")

;; Current keys & order
(def either-keys #{:either :expected :unexpected :exception})

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
  "Returns true if input is an either [:either ...] type"
  [input]
  (and (vector? input)
       (= (first input) :either)))

(defn get-value-type
  "Returns a keyword corresponding to the type of either.
  May be :unexpected :expected :exception or nil"
  [input]
  (and (either? input)
       (let [values (set input)]
         (first (filter #(not (contains? values %))
                        either-keys)))))

(defn unexpected?
  "Returns true if input is an unexpected either"
  [input]
  (= (get-value-type input) :unexpected))

(defn expected?
  "Returns true if input is an expected either"
  [input]
  (= (get-value-type input) :expected))

(defn exception?
  "Returns true if input is an exception either"
  [input]
  (= (get-value-type input) :exception))

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
  "Returns the value of a given [:either ...] type input"
  [input]
  (let [either-type (get-value-type input)]
    (cond (= either-type :unexpected) (get-unexpected input)
          (= either-type :expected) (get-expected input)
          (= either-type :exception) (get-exception input)
          :else input)))

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
       (forward results))
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
   (fn [input] (to-unexpected f input)))
  ([f input]
   (if (unexpected? input)
     (forward-using unexpected f (get-unexpected input))
     input)))

(defn on-unexpected
  "When the incoming either is unexpected use the given function to make it expected."
  ([f]
   (fn [input] (on-unexpected f input)))
  ([f input]
   (if (unexpected? input)
     (forward f (get-unexpected input))
     input)))

(defn on-expected
  "When the incoming either is expected use the given function against the
   expected value"
  ([f]
   (fn [input] (on-expected f input)))
  ([f input]
   (if (expected? input)
     (forward f (get-expected input))
     input)))

(defn to-exception
  "When the incoming either is an exception apply the given function against
  the exception value.
  Returns another exception value."
  ([f]
   (fn [input] (to-exception f input)))
  ([f input]
   (if (exception? input)
       (forward-using exception f (get-exception input))
       input)))

(defn on-exception
  "When the incoming either is an exception apply the given function against
  the exception value.
  Returns expected either if function is applied."
  ([f]
   (fn [results] (on-exception f results)))
  ([f input]
   (if (exception? input)
     (forward f (get-exception input))
     input)))

(defn when-unexpected
  "Run a predicate against the unexpected value, if return is truthy forward
   the return to an expected value."
  ([p f]
   (fn [input] (when-unexpected p f input)))
  ([p f input]
   (if (and (unexpected? input) (p (get-unexpected input)))
     (let [[type value] input]
       (forward f value))
     input)))

(defn when-expected
  "Run a predicate against the expected value, if return is truthy forward the
   expected value."
  ([p f]
   (fn [input] (when-expected p f input)))
  ([p f input]
   (if (and (expected? input) (p (get-expected input)))
     (let [[type unexpected value] input]
       (forward f value))
     input)))

(defn pipe
  "Run a function against an either and return the resulting value.
  Returns the result of applying the function to the either's value and does
  not return another either"
  ([f]
   (fn [input] (pipe f input)))
  ([f input]
   (if (either? input)
     (f (get-value input))
     input)))

(defn pipe-expected
  "Run a function against an expected either and return the raw result."
  ([f]
   (fn [input] (pipe-expected f input)))
  ([f input]
   (if (expected? input)
     (f (get-expected input))
     input)))

(defn pipe-unexpected
  "Run a function against an unexpected either and return the raw result."
  ([f]
   (fn [input] (pipe-unexpected f input)))
  ([f input]
   (if (unexpected? input)
     (f (get-unexpected input))
     input)))

(defn pipe-exception
  "Run a function against an exception either and return the raw result."
  ([f]
   (fn [input] (pipe-exception f input)))
  ([f input]
   (if (exception? input)
     (f (get-exception input))
     input)))

(defn tap
  "Run a function against the value of an either but return the original either.
  Good for side effects."
  ([f]
   (fn [input] (tap f input)))
  ([f input]
   (if (either? input)
     (do
       (f (get-value input))
       input)
     input)))

(defn tap-expected
  "Run a function against an expected either's value.
  Returns the expected either."
  ([f]
   (fn [input] (tap-expected f input)))
  ([f input]
   (doto input ((on-expected f)))))

(defn tap-unexpected
  "Run a function against an unexpected either's value.
  Returns the unexpected either."
  ([f]
   (fn [input] (tap-unexpected f input)))
  ([f input]
   (doto input ((on-unexpected f)))))

(defn tap-exception
  "Run a function against an exception either's value
  Returns the exception either."
  ([f]
   (fn [input] (tap-exception f input)))
  ([f input]
   (doto input ((on-exception f)))))
