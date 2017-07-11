(ns com.kaborso.blackjack.test-fixtures
  (:require [com.kaborso.blackjack.cards :as cards]))

; GAME
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

; TWEETS
(def latest-mentions
  [{:in_reply_to_screen_name "croupier_bot", :is_quote_status false, :coordinates nil,
    :in_reply_to_status_id_str nil, :place nil, :geo nil, :in_reply_to_status_id nil,
    :entities {:hashtags [], :symbols [], :user_mentions [{:screen_name "croupier_bot", :name "croupier_bot",
    :id 820654074, :id_str "820654074", :indices [0 11]}], :urls []}, :source "<a href=\"http://tapbots.com/tweetbot\" rel=\"nofollow\">Tweetbot for i?S</a>",
    :lang "en", :in_reply_to_user_id_str "820654074", :id 875232972466786304, :contributors nil, :truncated false, :retweeted false,
    :in_reply_to_user_id "820654074", :id_str "875232972466786304", :favorited false,
    :user {:description "", :profile_link_color "1DA1F2",
      :profile_sidebar_border_color "C0DEED", :is_translation_enabled false, :profile_image_url "http://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png",
      :profile_use_background_image true, :default_profile true, :profile_background_image_url nil, :is_translator false, :profile_text_color "333333", :name "kaborsino",
      :profile_background_image_url_https nil, :favourites_count 0, :screen_name "kaborsino",
      :entities
        {:description {:urls []}}, :listed_count 0, :profile_image_url_https "https://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png",
        :statuses_count 5, :has_extended_profile false, :contributors_enabled false, :following true, :lang "en", :utc_offset -25200,
        :notifications false, :default_profile_image true, :profile_background_color "F5F8FA", :id 742477679920881664, :follow_request_sent false,
        :url nil, :translator_type "none", :time_zone "Pacific Time (US & Canada)", :profile_sidebar_fill_color "DDEEF6", :protected true,
        :profile_background_tile false, :id_str "742477679920881664", :geo_enabled false, :location "", :followers_count 1, :friends_count 1,
        :verified false, :created_at "Mon Jun 13 22:04:08 +0000 2016"}, :retweet_count 0, :favorite_count 0,
        :created_at "Thu Jun 15 06:06:16 +0000 2017", :text "@croupier_bot test 3"}

    {:in_reply_to_screen_name "croupier_bot", :is_quote_status false, :coordinates nil,
     :in_reply_to_status_id_str nil, :place nil, :geo nil, :in_reply_to_status_id nil,
     :entities {:hashtags [], :symbols [],
        :user_mentions [{:screen_name "croupier_bot", :name "croupier_bot",
        :id 820654074, :id_str "820654074", :indices [0 11]}], :urls []},
      :source "<a href=\"http://tapbots.com/tweetbot\" rel=\"nofollow\">Tweetbot for i?S</a>",
      :lang "en", :in_reply_to_user_id_str "820654074", :id 875132302942253057,
      :contributors nil, :truncated false, :retweeted false,
      :in_reply_to_user_id 820654074, :id_str "875132302942253057",
      :favorited false,
      :user {:description "", :profile_link_color "1DA1F2",
        :profile_sidebar_border_color "C0DEED", :is_translation_enabled false,
        :profile_image_url "http://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png",
        :profile_use_background_image true, :default_profile true, :profile_background_image_url nil,
        :is_translator false, :profile_text_color 333333, :name "kaborsino", :profile_background_image_url_https nil,
        :favourites_count 0, :screen_name "kaborsino",
        :entities {:description {:urls []}},
        :listed_count 0, :profile_image_url_https "https://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png",
        :statuses_count 5, :has_extended_profile false, :contributors_enabled false, :following true, :lang "en",
        :utc_offset -25200, :notifications false, :default_profile_image true, :profile_background_color "F5F8FA",
        :id 742477679920881664, :follow_request_sent false, :url nil, :translator_type "none", :time_zone "Pacific Time (US & Canada)",
        :profile_sidebar_fill_color "DDEEF6", :protected true, :profile_background_tile false,
        :id_str "742477679920881664", :geo_enabled false, :location "",
        :followers_count 1, :friends_count 1, :verified false,
        :created_at "Mon Jun 13 22:04:08 +0000 2016"},
        :retweet_count 0, :favorite_count 0, :created_at "Wed Jun 14 23:26:14 +0000 2017", :text "@croupier_bot test 2"}])
