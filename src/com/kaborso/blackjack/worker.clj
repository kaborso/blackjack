(ns com.kaborso.blackjack.worker
  (:require [com.stuartsierra.component :as component]
            [clojure.core.async :as async]))

(defrecord Worker
  [storage input output process whistle]
  component/Lifecycle
  (start [component]
      (async/go-loop []
        (async/alt!
         input
         ([result]
           (async/go (async/>!! output (process component result)))
           (recur))
         whistle
          ([_] :yabadabadoo)))
      component)
  (stop [component]
    (async/go (async/>!! whistle :blow))
    (assoc component
           :storage nil
           :input nil
           :output nil
           :process nil
           :whistle nil)))
