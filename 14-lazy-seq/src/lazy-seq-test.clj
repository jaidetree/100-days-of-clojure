(ns lazy-seq-test)

(defn gen-positive-numbers
  ([] (gen-positive-numbers 0))
  ([n] (lazy-seq (cons n (gen-positive-numbers (inc n))))))

(def lazy-seq-numbers (take 5 (gen-positive-numbers)))
(def iterate-numbers (take 5(iterate inc 0)))
(def expected [0 1 2 3 4])

(println (str "Lazy sequence numbers == [0 1 2 3 4]: " (= lazy-seq-numbers expected)))
(println (str "Iterate numbers == [0 1 2 3 4]: " (= iterate-numbers expected)))
(println (str "Lazy sequence numbers == Iterate numbers: " (= lazy-seq-numbers iterate-numbers)))
