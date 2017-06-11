(ns com.kaborso.blackjack.test-fixtures
  (:require [com.kaborso.blackjack.cards :as cards]))

(def no-standing [])
(def player-one-standing [:kaborso])
(def player-two-standing [:kaborsino])
(def both-standing [:kaborso :kaborsino])
(def current-player 0)
(def two-player-current 0)
(def one-player [:kaborso])
(def two-players [:kaborso :kaborsino])
(def dealer-hand [(get (cards/deck) 0)])
(def dealer-hand-hard ["10♦" "2♥" "A♠"])
(def one-player-hand [ [(get (cards/deck) 1)] ])
(def one-player-winning-hand [ (subvec (cards/deck) 5 8) ])
(def one-player-blackjack-hand [ [(get (cards/deck) 0), (get (cards/deck) 10)] ])
(def one-player-bust-hand [ (subvec (cards/deck) 6 9) ])
(def second-player-bust-hands [ (subvec (cards/deck) 2 3) (subvec (cards/deck) 6 9) ])
(def two-player-bust-hands [ (subvec (cards/deck) 6 9) (subvec (cards/deck) 6 9)  ])
(def two-player-hands [ [(get (cards/deck) 1)] [(get (cards/deck) 2)] ])
(def one-player-deck (subvec (cards/deck) 2))
(def one-player-win-deck (subvec (cards/deck) 8))
(def two-player-deck (subvec (cards/deck) 3))
(def one-player-game [no-standing one-player dealer-hand one-player-winning-hand one-player-deck])
(def one-player-busts [no-standing one-player dealer-hand one-player-bust-hand one-player-deck])
(def one-player-stand [player-one-standing one-player dealer-hand one-player-winning-hand one-player-deck])
(def second-player-stand [player-two-standing two-players dealer-hand two-player-hands two-player-deck])
(def second-player-busts [no-standing two-players dealer-hand second-player-bust-hands two-player-deck])
(def one-player-blackjack [no-standing one-player dealer-hand one-player-blackjack-hand one-player-deck])
(def two-player-game [no-standing two-players dealer-hand two-player-hands two-player-deck])
(def two-player-stand [both-standing two-players dealer-hand two-player-hands two-player-deck])
(def two-player-bust [no-standing two-players dealer-hand two-player-bust-hands two-player-deck])
