(ns com.kaborso.blackjack.game
  (:require [com.stuartsierra.component :as component]
            [com.kaborso.blackjack.helpers :refer :all]
            [com.kaborso.blackjack.cards :as cards]
            [com.kaborso.blackjack.storage :as storage]
            [clojure.core.async :as async]))

(defrecord Game []
  component/Lifecycle

  (start [component]
    (println ";; Starting game server")
    (let [something (fn [])]
      (assoc component :gameserver something)))
  (stop [component]
    (println ";; Stopping game server")
    (assoc component :gameserver nil)))

(defn game [config] (->Game))

(def index-standing 0)
(def index-players 1)
(def index-dealer-hand 2)
(def index-player-hands 3)
(def index-deck 4)

(defn identifier
  "returns id for redis key"
  [tweeters]
  (reduce str (sort-by str tweeters)))

(defn player-keywords
  "turns tweeters from strings to keywords"
  [tweeters]
  (vec (map keyword tweeters)))

(defn first-hands
  "takes vectors for players and deck,
   returns vector containing hands for each players
   as well as the new state of the deck"
  [players deck]
  (let [num-players (count players),
        hands (vec (map vector (subvec deck 0 num-players))),
        new-deck (vec (drop num-players deck))]
        [hands, new-deck]))

(defn start
  [tweeters]
  (let [deck (cards/*shuffled-deck*)
        dealer-hand (first (cards/draw deck))
        player-hands-and-deck (first-hands tweeters (subvec deck 1))
        player-hands (first player-hands-and-deck)
        new-deck (second player-hands-and-deck)
        standing []
        players (player-keywords tweeters)]
        [standing players dealer-hand player-hands new-deck]))

; TODO test for state's return value
(defn state
  "Return the state of the current game the mentioned people are playing
   or return nil if one does not exist"
  [connection tweeter tweeters]
  (let [players (player-keywords tweeters)
        id (identifier tweeters)
        game (storage/retrieve connection id)]
        game))

; TODO test for state-or-new's return value
(defn state-or-new
  "Return the state of the current game the mentioned people are playing
   or returns a new game if one does not exist"
  [connection tweeter tweeters]
  (let [players (player-keywords tweeters)
        id (identifier tweeters)]
        (if-let [game (storage/retrieve connection id)]
          [tweeter, game]
          (let [game (start players)]
            (storage/put connection id game)
            [tweeter, game]))))

(defn store
  [connection tweeters game]
  (let [id (identifier tweeters)]
        (storage/put connection id game)))

(defn finish
  [connection tweeters game]
  (let [id (identifier tweeters)]
        (storage/archive connection id game)))

(defn all-stand?
  [game]
  (=  (get game index-standing)
      (get game index-players)))

; TODO memoize
(defn bust?
  [hand]
  (> (cards/tally hand) 21))

(defn all-bust?
  [game]
  (every? true? (map bust? (get game index-player-hands))))

(defn over?
  [game]
  (let [dealer (get game index-dealer-hand)
        players (get game index-player-hands)
        hands (into players [dealer])]
        (any? bust? hands)))
