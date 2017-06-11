(ns dev
  "Tools for interactive development with the REPL. This file should
  not be included in a production build of the application.

  Call `(reset)` to reload modified code and (re)start the system.

  The system under development is `system`, referred from
  `com.stuartsierra.component.repl/system`.

  See also https://github.com/stuartsierra/component.repl"
  (:require
   [clojure.java.io :as io]
   [clojure.java.javadoc :refer [javadoc]]
   [clojure.pprint :refer [pprint]]
   [clojure.reflect :refer [reflect]]
   [clojure.repl :refer [apropos dir doc find-doc pst source]]
   [clojure.set :as set]
   [clojure.string :as string]
   [clojure.test :as test :refer [run-tests]]
   [clojure.tools.namespace.repl :refer [refresh refresh-all clear]]
   [com.stuartsierra.component :as component]
   [com.stuartsierra.component.repl :refer [reset set-init start stop system]]
   [clojure.core.async :as async]
   [taoensso.carmine :as car]
   [com.kaborso.blackjack :as blackjack]
   [com.kaborso.blackjack.cards :as cards :refer [deck]]
   [com.kaborso.blackjack.game :refer [game]]
   [com.kaborso.blackjack.storage :as storage]
   [com.kaborso.blackjack.worker]
   [com.kaborso.blackjack.actions]
   [com.kaborso.blackjack.twitter]

   ))

;; Do not try to load source code from 'resources' directory
(clojure.tools.namespace.repl/set-refresh-dirs "dev" "src" "test")

(def dev-config
  {
   :host (System/getenv "BLACKJACK_DB_HOST")
   :port (System/getenv "BLACKJACK_DB_PORT")
   :consumer-key (System/getenv "BLACKJACK_KEY")
   :consumer-secret (System/getenv "BLACKJACK_SECRET")
   :access-token (System/getenv "BLACKJACK_ACCESS_TOKEN")
   :access-secret (System/getenv "BLACKJACK_ACCESS_SECRET")   })

(defn dev-system
  "Constructs a system map suitable for interactive development."
  []
  (blackjack/system dev-config))

(set-init (fn [_] (dev-system)))

(defn tests
  []
  (run-tests  'com.kaborso.blackjack-test
              'com.kaborso.blackjack.cards-test
              'com.kaborso.blackjack.game-test
              'com.kaborso.blackjack.twitter-test
              'com.kaborso.blackjack.actions-test))
