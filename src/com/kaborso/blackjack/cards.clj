(ns com.kaborso.blackjack.cards)

(defn card
  "Return rank and symbol"
  [rank symbol]
  (str rank symbol))

(defn numbered-cards
  "Return ranks for numbered cards"
  []
  (vec (take 9 (drop 2 (range)))))

(defn face-cards
  "Return ranks for face cards"
  []
  ["J" "Q" "K"])

(defn ace-card
  "Return rank for ace card"
  []
  ["A"])

(defn suit
  "Intermediary function to create a suit of 13 cards"
  [symbol]
  (vec (map #(card % symbol) (into (ace-card)
                              (into (numbered-cards)
                                      (face-cards))))))

(defn deck
  "Stardard 52-card deck"
  []
  (reduce into (map suit ["♣" "♦" "♥" "♠"])))

(defn ^:dynamic *shuffled-deck*
  []
  (shuffle (deck)))

(defn draw
  [deck]
  (let [hand  [(first deck)]
        new-deck (vec (drop 1 deck))]
  [hand new-deck]))

; TODO memoize?
(defn unsuit
  "Return rank for card"
  [card]
  (if (nil? card) nil (last (re-find #"^(.|10).$" card))))

(defn ace-value
  [current_total next_card]
  (if (nil? next_card)
    (if (> 21 (+ current_total 11)) 1 11)
    (if (> 21 (+ next_card current_total 11)) 1 11)))

(defn num-value
  [num]
  (Integer. num))

; TODO memoize?
(defn value
  "Return value for card"
  [card]
    (let [rank (unsuit card)]
      (case rank
        ("2" "3" "4"
         "5" "6" "7"
         "8" "9" "10")  (num-value rank)
         "A"            11
        ("K" "Q" "J")   10
               nil)))

(defn ace?
  [card]
  (not= nil (re-find #"^A.$" card)))

(defn gonna-bust?
  [card count]
  (> 21 (+ count (value card count))))

(defn ace-replace
  [value]
  (if (= 11 value) 1 value))

; TODO MEMOIZE
(defn tally
  [uncounted]
  (let [first_pass (map value uncounted)
        first_count (reduce + first_pass)]
        (if (< 21 first_count)
              (let [second_pass  (map ace-replace first_pass)]
                (reduce + second_pass))
              first_count)))

(defn total
  [cards]
  (let [found-aces (group-by ace? cards)
        ace-cards (get found-aces true)
        other-cards (get found-aces false)])
        (reduce tally cards))

(defn present?
  [hand rank]
  (some #(= rank %) (map unsuit hand)))

(defn soft?
  [hand]
  (and
    (= (tally hand) 11)
    (present? hand "A")))
