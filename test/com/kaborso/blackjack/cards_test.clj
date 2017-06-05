(ns com.kaborso.blackjack.cards-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [com.kaborso.blackjack.cards :refer :all]))

(deftest deck-tests
  (testing "52 Cards"
    (is (= 52 (count (deck)))))

  (testing "Standard Deck"
    (is (= ["A♣" "2♣" "3♣" "4♣" "5♣" "6♣" "7♣" "8♣" "9♣" "10♣" "J♣" "Q♣" "K♣"
             "A♦" "2♦" "3♦" "4♦" "5♦" "6♦" "7♦" "8♦" "9♦" "10♦" "J♦" "Q♦" "K♦"
             "A♥" "2♥" "3♥" "4♥" "5♥" "6♥" "7♥" "8♥" "9♥" "10♥" "J♥" "Q♥" "K♥"
             "A♠" "2♠" "3♠" "4♠" "5♠" "6♠" "7♠" "8♠" "9♠" "10♠" "J♠" "Q♠" "K♠"]
            (deck)))))

(deftest suit-tests
  (testing "Clubs"
    (is (= ["A♣" "2♣" "3♣" "4♣" "5♣" "6♣" "7♣" "8♣" "9♣" "10♣" "J♣" "Q♣" "K♣"]
            (suit "♣"))))
  (testing "Diamonds"
    (is (= ["A♦" "2♦" "3♦" "4♦" "5♦" "6♦" "7♦" "8♦" "9♦" "10♦" "J♦" "Q♦" "K♦"]
            (suit "♦"))))
  (testing "Hearts"
    (is (= ["A♥" "2♥" "3♥" "4♥" "5♥" "6♥" "7♥" "8♥" "9♥" "10♥" "J♥" "Q♥" "K♥"]
            (suit "♥"))))
  (testing "Spades"
    (is (= ["A♠" "2♠" "3♠" "4♠" "5♠" "6♠" "7♠" "8♠" "9♠" "10♠" "J♠" "Q♠" "K♠"]
            (suit "♠")))))

(deftest intermediary-function-tests
  (testing "Suit creation"
    (is (= ["A☭" "2☭" "3☭" "4☭" "5☭" "6☭" "7☭" "8☭" "9☭" "10☭" "J☭" "Q☭" "K☭"]
            (suit "☭"))))

  (testing "Ace"
    (is (= ["A"]
            (ace-card))))

  (testing "Face Cards"
    (is (= ["J" "Q" "K"]
            (face-cards))))

  (testing "Numbered Cards"
    (is (= [2 3 4 5 6 7 8 9 10]
            (numbered-cards))))

  (testing "Card creation"
    (is (= "A☭"
           (card "A" "☭"))))

  (testing "Suit removal"
    (is (= "A"
           (unsuit "A☭")))))

(deftest tally-tests
  (testing "Base"
    (is (= 17 (tally ["9♣" "8♥"])))
    (is (= 21 (tally ["9♣" "8♥" "4♠"])))
    (is (= 14 (tally ["5♦" "4♠" "3♣" "2♠"]))))

  (testing "Face Cards"
    (is (= 14 (tally ["4♦" "K♥"])))
    (is (= 20 (tally ["J♦" "K♦"])))
    (is (= 26 (tally ["Q♣" "6♠" "Q♥"])))
    (is (= 30 (tally ["J♣" "J♠" "Q♦"])))
    (is (= 25 (tally ["5♦" "J♥" "Q♣"]))))

  (testing "Ace Cards"
    (testing "Natural Hit"
      (is (= 21 (tally ["A♣" "10♦"])))
      (is (= 21 (tally ["J♥" "A♥"])))))

  (testing "Hard Hand"
    (is (= 20 (tally ["10♦" "9♥" "A♣"])))
    (is (= 21 (tally ["10♠" "10♥" "A♠"])))
    (is (= 17 (tally ["10♥" "6♣" "A♥"]))))

  (testing "Soft Hand"
    (is (= 17 (tally ["4♣" "5♥" "A♦" "7♥"])))
    (is (= 21 (tally ["7♦" "3♣" "A♥" "10♠"])))))
