(ns pegthing-test
  (:require [clojure.test :refer :all]
            [pegthing.core :refer :all]))

;; Continuation of yesterday's work.
;; This time I used some examples from the book to guide the tests
;; even found the function that was broken and now the game works.

(def my-board (assoc-in (new-board 5) [4 :pegged] false))

(deftest tri-test
  (testing "Tri Test"
    (is (= (last (take 1 tri)) 1))
    (is (= (last (take 3 tri)) 6))
    (is (= (last (take 6 tri)) 21))))

(deftest triangular-test
  (testing "Triganular returns true on triangle numbers"
    (is (= (triangular? 1) true))
    (is (= (triangular? 3) true))
    (is (= (triangular? 6) true))
    (is (= (triangular? 5) false))))

(deftest row-tri-test
  (testing "Row-tri gets the last tri of a given length"
    (is (= (row-tri 1) 1))
    (is (= (row-tri 3) 6))
    (is (= (row-tri 4) 10))))

(deftest row-num-test
  (testing "Row num returns the row that contains a tri"
    (is (= (row-num 1) 1))
    (is (= (row-num 6) 3))
    (is (= (row-num 10) 4))))

(deftest connect-test
  (testing "Connects two items in a board"
    (is (= (connect {} 10 1 3 6)
           {1 {:connections {6 3}}, 6 {:connections {1 3}}}))))

(deftest connect-right-test
  (testing "Connects pegs to the right"
    (is (= (connect-right {} 6 3))
        {1 {:connections {6 3}} 6 {:connections {1 3}}})))

(deftest connect-down-left-test
  (testing "Connects pegs to the down left"
    (is (= (connect-down-left {} 8 3)
           {3 {:connections {8 5}} 8 {:connections {3 5}}}))))

(deftest connect-down-right-test
  (testing "Connects pegs down right"
    (is (= (connect-down-right {} 15 6)
           {6 {:connections {15 10}} 15 {:connections {6 10}}}))))

(deftest add-pos-test
  (testing "Updates the board with new peg"
    (is (= (add-pos {} 8 3)
           {3 {:pegged true, :connections {8 5}}, 8 {:connections {3 5}}}))))

(deftest new-board-test
  (testing "Creates a new board map with set number of rows"
    (is (= (new-board 2)
           {:rows 2, 1 {:pegged true}, 2 {:pegged true}, 3 {:pegged true}}))))

(deftest pegged-test
  (testing "Returns true when target peg is pegged"
    (is (= (pegged? {3 {:pegged true, :connections {8 5}}, 8 {:connections {3 5}}} 3)
           true)))
  (testing "Returns false when target peg is not pegged"
    (is (= (pegged? {3 {:pegged false, :connections {8 5}}, 8 {:connections {3 5}}} 3)
           false))))

(deftest remove-peg-test
  (testing "Returns board with :pegged set to false"
    (is (= (remove-peg {3 {:pegged true, :connections {8 5}}, 8 {:connections {3 5}}} 3)
           {3 {:pegged false, :connections {8 5}}, 8 {:connections {3 5}}}))))

(deftest place-peg-test
  (testing "Returns board with target peg :pegged set to true"
    (is (= (place-peg {3 {:pegged false, :connections {8 5}}, 8 {:connections {3 5}}} 3)
           {3 {:pegged true, :connections {8 5}}, 8 {:connections {3 5}}}))))

(deftest move-peg-test
  (testing "Returns board with pegged true on target and false on source"
    (is (= (move-peg {3 {:pegged true, :connections {8 5}}, 8 {:pegged false, :connections {3 5}}} 3 8)
           {3 {:pegged false, :connections {8 5}}, 8 {:pegged true :connections {3 5}}}))))

(deftest valid-moves-test
  (testing "Returns map of valid moves for a given pos"
    (is (= (valid-moves my-board 1)
           {4 2}))
    (is (= (valid-moves my-board 6)
           {4 5}))
    (is (= (valid-moves my-board 11)
           {4 7}))
    (is (= (valid-moves my-board 5)
           {}))
    (is (= (valid-moves my-board 8)
           {}))))

(deftest valid-move-test
  (testing "Returns a positive integer when pos1 to pos2 is a valid move otherwise nil"
    (is (= (valid-move? my-board 8 4) nil))
    (is (= (valid-move? my-board 1 4) 2))))

(deftest make-move-test
  (testing "Removes jumped peg from board"
    (is (= (get-in (make-move my-board 1 4) [2 :pegged])
           false))
    (is (= (make-move my-board 1 6)
           nil))))

(deftest can-move?-test
  (testing "Returns true when board has valid moves"
    (is (= (can-move? my-board) {4 2}))
    (is (= (can-move? (new-board 5)) nil))))
