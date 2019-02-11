(ns abc.views
  (:require
   [react :refer (Fragment)]
   [citrus.core :as citrus]
   [rum.core :as rum]
   [abc.db :as db]))

(defn Emoji [emoji]
  [:.circle
   [:.emoji emoji]])

(defn Word [word]
  [:.word word])

(defn Letter [char state]
  [:span {:class ["letter"
                  (case state
                    :correct "letter__correct"
                    :incorrect "letter__incorrect"
                    nil)]} char])

(rum/defc Main < rum/reactive [r]
  (let [letter-info (db/get-letter-info (db/state r))
        state (:state (db/state r))]
    [:main {:key "main"}
     (Emoji (:emoji letter-info))
     (Word (:word letter-info))
     (Letter (:char letter-info) state)]))

(defn Header []
  [:header.header {:key "header"}
   [:div.header__inner
    [:div.logo "Emoji ABC"]
    [:span.hidden
     [:span
      [:span.option {:key "word"} "ord"]
      [:span {:key "separator"} "/"]
      [:span.option.option--selected {:key "letter"} "bokstav"]]
     [:span
      [:span.option {:key "word"} "sv"]
      [:span {:key "separator"} "/"]
      [:span.option.option--selected {:key "letter"} "en"]]]]])

(rum/defc App [r]
  [Fragment (Header) (Main r)])