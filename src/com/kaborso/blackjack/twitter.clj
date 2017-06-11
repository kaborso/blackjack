(ns com.kaborso.blackjack.twitter
  (:require [com.stuartsierra.component :as component]
            [twitter.oauth]
            [twitter.callbacks]
            [twitter.callbacks.handlers]
            [twitter.api.restful :as rest-api]
            [com.kaborso.blackjack.cards :as cards]
            [clojure.core.async :as async])
  (:import [twitter.callbacks.protocols SyncSingleCallback]))

(defrecord Tweeter
  [oauth-credentials input tweet! stop-posting]
  component/Lifecycle
  (start [component]
    (println ";; Ready to tweet")
    (async/go-loop []
      (async/alt!
       input
       ([tweeter tweet-id message]
         (async/go (tweet! component tweeter tweet-id message))
         (recur))
       stop-posting
        ([_] :logoff)))
    component)

  (stop [component]
    (println ";; Logging off")
    (async/go (async/>!! stop-posting :now))
    (assoc component
          :input nil
          :stop-posting nil)))

(defn twitter-client
   [consumer-key consumer-secret access-token access-secret]
   (twitter.oauth/make-oauth-creds consumer-key consumer-secret access-token access-secret))

(comment
  (defn streaming-callback
    [tweet]
    (if (mention? tweet)
      (analyze tweet)
      :do-nothing))

(rest-api/statuses-mentions-timeline))

(defn hand-string
  [cards]
  (reduce #(str %1 " " %2) cards))

(defn tweet-format
  [tweeter [standing players dealer-hand player-hands deck]]
  (let [current-player (keyword tweeter)
        position (.indexOf players current-player)
        player-hand (get player-hands position)
        player-hand-string (hand-string player-hand)
        player-total (cards/tally player-hand)
        dealer-hand-string (hand-string dealer-hand)
        dealer-total (cards/tally dealer-hand)]
        (str "-----\n"
             "Dealer: " dealer-hand-string " (" dealer-total ")\n"
             "Your Hand: " player-hand-string " (" player-total ")")))

(defn reply
  [component tweeter tweet-id text]
  (let [status (str "@" tweeter " " text)
        credentials (:twitter component)]
    (comment
      (rest-api/statuses-update
        :oauth-creds credentials
        :params {:status status
        :in_reply_to_status_id tweet-id})))
    {:reply-to tweet-id
     :receiver tweeter
     :message text})
