(ns com.kaborso.blackjack.actions-test
  (:require
   [clojure.test :refer [deftest is are testing]]
   [com.kaborso.blackjack.actions :refer :all]
   [com.kaborso.blackjack.cards :as cards]
   [com.kaborso.blackjack.test-fixtures :refer :all]))

(deftest player-tweet-tests
  (testing "Hit?"
    (testing "match /^hit/ 'hit'" )
      (is (hit? "hit"))
    (testing "match /^hit/ 'HIT'" )
      (is (hit? "HIT"))
    (testing "match /^hit/ 'hit me'"
      (is (not (hit? "hit me"))))
    (testing "match /^hit/ 'hit me'"
      (is (not (hit? "hit me"))))
    (testing "match /^hit/ 'give me a hit'"
      (is (not (hit? "give me a hit"))))
    (testing "match /^hit/ 'shit'"
      (is (not (hit? "shit"))))
    (testing "match /^hit/ 'h i t'"
      (is (not (hit? "h i t"))))
    (testing "match /^hit/ 'hitting'"
      (is (not (hit? "hitting")))))
  (testing "Game?"
    (testing "match /^game/ 'game'"
      (is (game? "game"))))
  (testing "Surrender?"
    (testing "match /^surrender/ 'surrender'"
      (is (surrender? "surrender"))))
  (testing "Stand?"
    (testing "match /^stand/ 'stand'"
      (is (stand? "stand")))))

(deftest player-action-tests
  (testing "stand"
    (is (=  ["standing_player" [[:standing_player] [:standing_player] [] [] []]]
            (stand "standing_player" [ [] [:standing_player] [] [] [] ])))
    (is (=  ["kaborso" one-player-stand]
            (stand "kaborso" one-player-game))))
  (testing "hit"
    (testing "single player"
      (is (=  ["kaborso" [ no-standing one-player dealer-hand [["2♣" "3♣"]] (subvec (cards/deck) 3) ]]
              (hit "kaborso" [ no-standing one-player dealer-hand one-player-hand one-player-deck ]))))
    (testing "multiplayer"
      (is (=  ["kaborsino" [ no-standing two-players dealer-hand [["2♣"] ["3♣" "4♣"]] (subvec (cards/deck) 4) ]]
              (hit "kaborsino" [ no-standing two-players dealer-hand two-player-hands two-player-deck ]))))
    (testing "player-actions fun"
      (testing "no action for tweet text"
        ; TODO
      ))))



(deftest dealer-action-tests
  (testing "dealer-reveal"
    (is (= [[:kaborso] [:kaborso] ["A♣" "3♣" "4♣" "5♣" "6♣"] [["6♣" "7♣" "8♣"]] (subvec (cards/deck) 6)]
    (dealer-reveal one-player-stand))))
  (testing "dealer-hit"
    (testing "hard"
      (testing "hit until 17 or more"
        (is (= ["10♦" "2♥" "A♠" "3♣" "4♣"] (first (dealer-hit ["10♦" "2♥" "A♠"] one-player-deck))))))
    (testing "soft"
      (testing "hit at 17"
        (is (= ["A♣" "3♥" "3♦" "3♣"] (first (dealer-hit ["A♣" "3♥" "3♦"] one-player-deck)))))
      (testing "hit when over 17 but safe"
        (is (= ["A♣" "3♥" "4♦" "3♣"] (first (dealer-hit ["A♣" "3♥" "4♦"] one-player-deck))))))))
