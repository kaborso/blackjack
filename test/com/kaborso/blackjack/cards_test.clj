(ns com.kaborso.blackjack.cards-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [com.kaborso.blackjack.cards :refer :all]))

(deftest deck-tests
  (testing "52 Cards"
    (is (= 52 (count (deck)))))

  (testing "Standard Deck"
    (is (= '("A♣" "2♣" "3♣" "4♣" "5♣" "6♣" "7♣" "8♣" "9♣" "10♣" "J♣" "Q♣" "K♣"
             "A♦" "2♦" "3♦" "4♦" "5♦" "6♦" "7♦" "8♦" "9♦" "10♦" "J♦" "Q♦" "K♦"
             "A♥" "2♥" "3♥" "4♥" "5♥" "6♥" "7♥" "8♥" "9♥" "10♥" "J♥" "Q♥" "K♥"
             "A♠" "2♠" "3♠" "4♠" "5♠" "6♠" "7♠" "8♠" "9♠" "10♠" "J♠" "Q♠" "K♠")
            (deck)))))

(deftest suit-tests
  (testing "Clubs"
    (is (= '("A♣" "2♣" "3♣" "4♣" "5♣" "6♣" "7♣" "8♣" "9♣" "10♣" "J♣" "Q♣" "K♣")
            (suit "♣"))))
  (testing "Diamonds"
    (is (= '("A♦" "2♦" "3♦" "4♦" "5♦" "6♦" "7♦" "8♦" "9♦" "10♦" "J♦" "Q♦" "K♦")
            (suit "♦"))))
  (testing "Hearts"
    (is (= '("A♥" "2♥" "3♥" "4♥" "5♥" "6♥" "7♥" "8♥" "9♥" "10♥" "J♥" "Q♥" "K♥")
            (suit "♥"))))
  (testing "Spades"
    (is (= '("A♠" "2♠" "3♠" "4♠" "5♠" "6♠" "7♠" "8♠" "9♠" "10♠" "J♠" "Q♠" "K♠")
            (suit "♠")))))

(deftest intermediary-functions-tests
  (testing "Suit creation"
    (is (= '("A☭" "2☭" "3☭" "4☭" "5☭" "6☭" "7☭" "8☭" "9☭" "10☭" "J☭" "Q☭" "K☭")
            (suit "☭"))))

  (testing "Ace"
    (is (= "A"
            (ace-card))))

  (testing "Face Cards"
    (is (= '("J" "Q" "K")
            (face-cards))))

  (testing "Numbered Cards"
    (is (= '(2 3 4 5 6 7 8 9 10)
            (numbered-cards))))

  (testing "Card creation"
    (is (= "A☭"
           (card "A" "☭")))))
