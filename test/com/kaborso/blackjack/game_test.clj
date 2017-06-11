(ns com.kaborso.blackjack.game-test
  (:require
   [clojure.test :refer [deftest is are testing]]
   [com.kaborso.blackjack.game :refer :all]
   [com.kaborso.blackjack.cards :as cards]
   [com.kaborso.blackjack.test-fixtures :refer :all]))

(deftest victory-condition-tests
  (testing "standing"
    (testing "no standing"
      (is (= false (all-stand? one-player-game))))
    (testing "one player stands"
     (is (= true (all-stand? one-player-stand))))
    (testing "second player stands"
      (is (= false (all-stand? second-player-stand))))
    (testing "both stand"
      (is (= true (all-stand? two-player-stand)))))

    (testing "busting"
      (testing "no busts"
        (is (= false (all-bust? one-player-game))))
      (testing "one player busts"
       (is (= true (all-bust? one-player-busts))))
      (testing "second player busts"
        (is (= false (all-bust? second-player-busts)))
        (is (= true (bust? (second second-player-bust-hands)))))
      (testing "both bust"
        (is (= true (all-bust? two-player-bust)))))

  (testing "game over?"
    (is (= true (over? [both-standing two-players ["J♦" "K♦"] [["A♣" "10♦"] ["Q♣" "6♠" "Q♥"]] (cards/deck)])))))

(deftest initialization-tests

  (testing "first-hands"
    (is (= [[[6], [7]], [8, 9]]
        (first-hands ["bob", "joe"] [6, 7, 8, 9]))))
  (testing "new-game"
    (testing "one player"
      (is (= [no-standing one-player dealer-hand one-player-hand one-player-deck]
              (binding [com.kaborso.blackjack.cards/*shuffled-deck* com.kaborso.blackjack.cards/deck]
                (start ["kaborso"])))))
    (testing "two players"
      (is (= [no-standing two-players dealer-hand two-player-hands two-player-deck]
              (binding [com.kaborso.blackjack.cards/*shuffled-deck* com.kaborso.blackjack.cards/deck]
                (start ["kaborso", "kaborsino"])))))
    (testing "four players" )
    (testing "too many players" )
    (testing "game state structure"
      (let [test_game (start ["jill"])]
        (testing "should contain five sections"
          (is (= 5 (count test_game))))
        (testing "player collection should contain keywords"
           (is (= true (every? keyword? (get test_game index-players)))))
        (testing "player collection should contain keywords"
           (is (= true (every? keyword? (get test_game index-players)))))
         (testing "dealer hand should contain strings"
          (is (= true (every? string? (get test_game index-dealer-hand)))))
        (testing "player hands should contain strings"
          (is (= true (every? true? (map #(every? string? %) (get test_game index-player-hands))))))
        (testing "deck should contain strings"
          (is (= true (every? string? (get test_game index-deck)))))))))
