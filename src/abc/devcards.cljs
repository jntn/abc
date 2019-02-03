(ns abc.devcards
  (:require [devcards.core :refer-macros (defcard)]
            [abc.views :as views]
            [devcards.core :as dc]
            [sablono.core :as sab]
            [reagent.core :as r]))

(defcard header
  (sab/html (views/header))
  {}
  {:padding false})

(defcard main
  (sab/html (views/main)))

(defcard current-letter
  "The current letter that should be matched"
  (sab/html (views/letter "A" :none)))

(defcard correct-letter
  "The current letter when the *correct* letter has been entered"
  (sab/html (views/letter "B" :correct)))

(defcard incorrect-letter
  "The current letter when the *incorrect* letter has been entered"
  (sab/html (views/letter "C" :incorrect)))

(defcard emoji
  "The emoji that represents the letter"
  (sab/html (views/emoji)))

(defn ^:export main
  []
  (dc/start-devcard-ui!))

