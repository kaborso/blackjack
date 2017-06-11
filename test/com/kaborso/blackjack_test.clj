(ns com.kaborso.blackjack-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [com.stuartsierra.component :as component]
    [com.kaborso.blackjack.helpers :refer :all]
    [com.kaborso.blackjack.storage :as storage :refer [new-storage]]
    [com.kaborso.blackjack.worker :refer :all]
    [com.kaborso.blackjack.cards :as cards]
    [com.kaborso.blackjack.game :refer [game]]
    [com.kaborso.blackjack.actions :refer [player-actions dealer-actions tweet-preparation]]
    [com.kaborso.blackjack.twitter :as twitter :refer [twitter-client map->Tweeter]]
    [com.kaborso.blackjack :as blackjack]
    [clojure.core.async :as async]))

(def test-configuration
  { :host (env "BLACKJACK_DB_HOST")
    :port (env "BLACKJACK_DB_PORT")
    :consumer-key (env "BLACKJACK-KEY")
    :consumer-secret (env "BLACKJACK-SECRET")
    :access-token (env "BLACKJACK-ACCESS-TOKEN")
    :access-secret (env "BLACKJACK-ACCESS-SECRET")
    :account-handle (env "BLACKJACK-ACCOUNT-HANDLE")})

(defrecord TestTweeter
  [oauth-credentials input tweet! stop-posting]
  component/Lifecycle
  (start [component]
    (println ";; Ready to tweet")
    (async/go-loop []
      (async/alt!
       input
        ([[worker tweeter tweet-id message]]
          (async/go
            (tweet! component tweeter tweet-id message))
          (async/>! input message) ; Re-enqueue message for testing
          (recur))
        stop-posting
        ([_] :logoff)))
    component)
  (stop [component]
    (println ";; Halting posts")
    (async/go
      (async/>!! stop-posting :now))
    (assoc component
          :input nil
          :stop-posting nil)))

(defn test-system [config-options]
  (let [{:keys [host port consumer-key
                consumer-secret
                access-token access-secret reply-function]} config-options]
    (-> (component/system-map
          :storage (new-storage host port)
          :twitter (twitter-client consumer-key
                    consumer-secret access-token access-secret)
          :reading (async/chan)
          :playing (async/chan)
          :dealing (async/chan)
          :sending (async/chan)
          :player (component/using
                    (map->Worker {  :process player-actions
                                    :whistle (async/chan 1)})
                                    { :input :reading
                                      :output :playing
                                      :storage :storage})
          :dealer (component/using
                    (map->Worker {:process dealer-actions
                                  :whistle (async/chan 1)})
                                  {:input :playing
                                    :output :dealing
                                    :storage :storage})
          :render (component/using
                    (map->Worker
                      { :process tweet-preparation
                        :whistle (async/chan 1)})
                    { :input :dealing
                      :output :sending
                      :storage :storage})
          :sender (component/using
                    (map->TestTweeter { :tweet! (fn [& rest] :do-nothing)
                                    :stop-posting (async/chan 1) })
                    { :input :sending
                       :credentials :twitter})
          :app (game config-options))
        (component/system-using
         {:app [:storage :player :dealer :render :sender]}))))

(deftest blackjack-integration-tests
  (binding [com.kaborso.blackjack.cards/*shuffled-deck* com.kaborso.blackjack.cards/deck]
    (def system (component/start-system (test-system test-configuration)))
    (def testuser "testuser")
    (def ^:dynamic timeout-ch nil)
    (def ^:dynamic result nil)
    (storage/delete (:storage system) testuser)
    (testing "new game"
        (async/go
          (async/>!! (:reading system)
            [testuser, [(keyword testuser)], "game"]))
        (binding
          [timeout-ch (async/timeout 10000)
          result (async/alt!!
                  [(:sending system)] ([tweet _] tweet))
                  timeout-ch  :timed-out]
           (is (=  (str "-----\n"
                        "Dealer: A♣ (11)\n"
                        "Your Hand: 2♣ (2)")
                        result))))
    (testing "hit"
      (async/go
        (async/>!! (:reading system)
          [testuser, [(keyword testuser)], "hit"]))
        (binding
          [timeout-ch (async/timeout 10000)
          result (async/alt!!
                  [(:sending system)] ([tweet _] tweet))
                  timeout-ch :timed-out]
          (is (= (str "-----\n"
                      "Dealer: A♣ (11)\n"
                      "Your Hand: 2♣ 3♣ (5)")
                      result))))
    (testing "stand"
        (async/go
          (async/>!! (:reading system)
            [testuser, [(keyword testuser)], "stand"]))
        (binding
          [timeout-ch (async/timeout 10000)
          result (async/alt!!
                  [(:sending system)] ([tweet _] tweet))
                  timeout-ch :timed-out]
          (is (= (str "-----\n"
                      "Dealer: A♣ 4♣ 5♣ (20)\n"
                      "Your Hand: 2♣ 3♣ (5)")
                      result))))))
