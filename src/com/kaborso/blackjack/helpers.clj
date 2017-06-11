(ns com.kaborso.blackjack.helpers)

(defn env
  [var]
  (System/getenv (name var)))

(defn match?
  [pattern text]
  (not= nil (re-find (re-pattern pattern) text)))

(def any? (complement not-any?))
