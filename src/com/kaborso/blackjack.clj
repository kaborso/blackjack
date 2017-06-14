(ns com.kaborso.blackjack
  (:require [com.stuartsierra.component :as component]
            [com.kaborso.blackjack.helpers :refer [env]]
            [com.kaborso.blackjack.game :refer [game]]
            [com.kaborso.blackjack.storage :refer [new-storage]]
            [com.kaborso.blackjack.twitter :as twitter :refer [twitter-client map->Tweeter]]
            [com.kaborso.blackjack.worker :refer :all]
            [com.kaborso.blackjack.actions :refer [player-actions dealer-actions tweet-preparation]]
            [clojure.core.async :as async]
            [clojure.tools.logging :as log]))


(def configuration
  {
   :host (env "BLACKJACK_DB_HOST")
   :port (env "BLACKJACK_DB_PORT")
   :consumer-key (env "BLACKJACK_KEY")
   :consumer-secret (env "BLACKJACK_SECRET")
   :access-token (env "BLACKJACK_ACCESS_TOKEN")
   :access-secret (env "BLACKJACK_ACCESS_SECRET")
   :account-handle (env "BLACKJACK_ACCOUNT_HANDLE")
   :reply-function twitter/reply})

(defn system [config-options]
  (let [{:keys [host port consumer-key
                consumer-secret
                access-token access-secret reply-function]} config-options]
    (-> (component/system-map
          ; :storage (map->Storage [host port] )
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
                    (map->Tweeter { :tweet! reply-function
                                    :stop-posting (async/chan 1) })
                    { :input :sending
                       :credentials :twitter})
          :app (game config-options))
        (component/system-using
         {:app [:storage :player :dealer :render :sender]}))))

(def blackjack (system configuration))

(defn main [] (component/start-system blackjack))
