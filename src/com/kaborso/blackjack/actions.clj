(ns com.kaborso.blackjack.actions
  (:require [com.kaborso.blackjack.helpers :refer :all]
            [com.kaborso.blackjack.cards :as cards]
            [com.kaborso.blackjack.storage :as storage]
            [com.kaborso.blackjack.twitter :as twitter]
            [com.kaborso.blackjack.game :as game]))

(defn game?
  [text]
  (match? "^(?i)game$" text))

(defn hit?
  [text]
  (match? "^(?i)hit$" text))

(defn stand?
  [text]
  (match? "^(?i)stand$" text))

(defn surrender?
  [text]
  (match? "^(?i)surrender$" text))

(defn stand
  [tweeter game]
  (let  [player (keyword tweeter)
        [standing players dealer hands deck] game
        updated-standing (into standing [player])
        updated-game [updated-standing players dealer hands deck]]
        [tweeter updated-game]))

(defn surrender
  [player game]
  (let  [updated-game game]
        [player updated-game]))

(defn dealer-hit
  [hand deck]
  (if (<= (cards/tally hand) 18)
    (dealer-hit (into hand (subvec deck 0 1)) (subvec deck 1))
    [hand deck]))

(defn dealer-reveal
  [game]
  (let [[ s p hand h deck ] game
        [new-hand new-deck] (dealer-hit hand deck)]
        [ s p new-hand h new-deck]))

(defn hit
  [tweeter game]
  (if (= nil game)
      [tweeter :nil]
      (let [[standing players dealer-hand player-hands deck] game
            current-player (.indexOf players (keyword tweeter))
            player-hand (get player-hands current-player)
            updated-hand-and-deck (cards/draw deck)
            updated-player-hand (into player-hand (first updated-hand-and-deck))
            updated-player-hands (assoc player-hands current-player updated-player-hand)
            updated-game [standing players dealer-hand updated-player-hands (second updated-hand-and-deck)]]
            [tweeter updated-game])))

(defn player-update
  [[player game]]
  ;; do something I guess
  [player [game]])

(defn player-actions
  [worker [tweeter tweeters tweet]]
  (cond
    (game? tweet)
      (player-update
        (game/state-or-new (:storage worker) tweeter tweeters))
    (hit? tweet)
      (player-update
        (hit tweeter
          (game/state (:storage worker) tweeter tweeters)))
    (surrender? tweet)
      (player-update
        (surrender tweeter
          (game/state (:storage worker) tweeter tweeters)))
    (stand? tweet)
      (player-update
        (stand tweeter
          (game/state (:storage worker) tweeter tweeters)))
    :else [tweeter, :cmd]))

(defn dealer-actions
  [worker [player [game_or_keyword]]]
  (if (keyword? game_or_keyword)
    [player game_or_keyword]
    (let
      [game (if (or (game/all-bust? game_or_keyword)
                    (game/all-stand? game_or_keyword))
              (dealer-reveal game_or_keyword)
              game_or_keyword)]
      (if (game/over? game)
        (game/finish (:storage worker) [player] game)
        (game/store (:storage worker) [player] game))
      [player game])))

(defn tweet-preparation
  [worker [player game]]
  (cond
      (vector? game)  [worker player 0 (twitter/tweet-format player game)]
      (= game :nil)   [worker player 0 "Game does not exist. Tweet the word 'game' at me to start one."]
      (= game :cmd)   [worker player 0 "Commands: Game, Hit, Stand, Surrender"]
      :else           [worker player 0 "Sorry, something has gone very wrong"]))
