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

(deftest mentions-tests
  (testing "mention"
    (testing "all fields"
      (is (=  { :text "@croupier_bot test 3"
                :time "Thu Jun 15 06:06:16 +0000 2017"
                :id_str "875232972466786304"
                :screen_name "kaborsino"
                :name "kaborsino"
                :retweeted false
                :is_quote_status false
                :in_reply_to_screen_name "croupier_bot"
                :in_reply_to_user_id_str "820654074"
                :in_reply_to_status_id_str nil
                :user_mentions
                [{:screen_name "croupier_bot",
                  :name "croupier_bot",
                  :id 820654074,
                  :id_str "820654074",
                  :indices [0 11]}]}
              (mention (first latest-mentions)))))
     (testing "empty fields")
     (testing "missing fields"))
  (testing "mentions-to-queue"
    (testing "mentions not queued"
      (let [queued-mentions [875131973676781569, 875125297691193349]
            latest-mentions [875232972466786304, 875132302942253057, 875131973676781569, 875125297691193349]]
        (is (= [875232972466786304, 875132302942253057]
               (mentions-to-queue queued-mentions latest-mentions)))))
    (testing "mentions already queued")
      (let [queued-mentions [875232972466786304, 875132302942253057, 875131973676781569, 875125297691193349]
            latest-mentions [875232972466786304, 875132302942253057, 875131973676781569, 875125297691193349]]
        (is (= [] (mentions-to-queue queued-mentions latest-mentions))))))
