(ns abc.views
  (:require
   [react :refer (Fragment)]
   [citrus.core :as citrus]
   [rum.core :as rum]
   [abc.db :as db]))

(defn Emoji [emoji]
  [:div {:class "circle"} [:span {:class "emoji"} emoji]])

(defn Letter [char state]
  [:span {:class ["letter"
                  (case state
                    :correct "letter__correct"
                    :incorrect "letter__incorrect"
                    nil)]} char])

(rum/defc Main < rum/reactive [r]
  (let [state (rum/react (db/state r))
        letter-index (:current-letter state)
        letter-list (:letter-list state)
        letter (nth letter-list letter-index)
        letter-info (get-in state [:letters letter])]
    [:main {:key "main"}
     (Emoji (get-in letter-info [:sv :emoji])) (Letter (:letter letter-info) :none)]))

(defn Header []
  [:header {:class "header" :key "header"}
   [:div {:class "header__inner"}
    [:div {:class "logo"} "Emoji ABC"]
    [:div [:span {:class "option" :key "word"} "ord"] [:span {:key "separator"} "/"] [:span {:class "option option--selected" :key "letter"} "bokstav"]]]])

(rum/defc App [r]
  [Fragment (Header) (Main r)])