(ns com.kaborso.blackjack.twitter-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [com.kaborso.blackjack.twitter :refer :all]
   [com.kaborso.blackjack.test-fixtures :refer :all]
))

(deftest format-tests
  (testing "hand-string"
    (is (=  "6♣ 7♣ 8♣"
            (hand-string ["6♣" "7♣" "8♣"]))))
  (testing "tweet-format"
    (is (=  (str "-----\n"
              "Dealer: A♣ (11)\n"
              "Your Hand: 6♣ 7♣ 8♣ (21)")
            (tweet-format "kaborso" one-player-game)))))
