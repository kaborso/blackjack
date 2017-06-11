(ns com.kaborso.blackjack.storage
  (:require [com.stuartsierra.component :as component]
            [taoensso.carmine :as car :refer (wcar)]))

(defmacro wcar* [connection & body] `(car/wcar ~connection ~@body))

(defrecord Storage
  [host port]
  component/Lifecycle

  (start [component]
    (println ";; Starting storage")
    component)

  (stop [component]
    (println ";; Stopping storage")
    component))

(defn new-storage [host port]
  (map->Storage {:host host :port port}))

(defn ping
  [connection]
  (wcar* connection (car/ping)))

(defn put
  [connection key map]
  (wcar* connection (car/set key (car/freeze map))))

(defn retrieve
  [connection key]
  (wcar* connection (car/get key)))

(defn delete
  [connection key]
  (wcar* connection (car/del key)))

(defn archive
  [connection game_key map]
  (wcar*
   connection
   (car/del game_key)
   (car/hset (str game_key ":archive") game_key (car/freeze map))))
