(ns com.kaborso.blackjack.cards)

(defn card
  "Return rank and symbol"
  [rank symbol]
  (str rank symbol))

(defn numbered-cards
  "Return ranks for numbered cards"
  []
  (take 9 (drop 2 (range))))

(defn face-cards
  "Return symbols for face cards"
  []
  '("J" "Q" "K"))

(defn ace-card
  "Return symbol for ace card"
  []
  "A")

(defn suit
  "Intermediary function to create a suit of 13 cards"
  [symbol]
  (map #(card % symbol) (cons (ace-card)
                              (concat (numbered-cards)
                                      (face-cards)))))

(defn deck
  "Stardard 52-card deck"
  []
  (reduce concat (map suit ["♣" "♦" "♥" "♠"])))
