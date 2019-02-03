(ns abc.db
  (:require [citrus.core :as citrus]))

(def initial-state {:chosen-language :sv
                    :current-letter 0
                    :letter-list [:a :b]
                    :letters {:a
                              {:letter "A"
                               :sv {:emoji "🐵" :word "APA"}
                               :en {:emoji "🍏" :word "APPLE"}}
                              :b
                              {:letter "B"
                               :sv {:emoji "🌺" :word "BLOMMA"}
                               :en {:emoji "🐝" :word "BEE"}}}})

(defmulti control (fn [event] event))

(defmethod control :init [event args state]
  {:state initial-state})

(defmethod control :enter-letter [event args state]
  {:state (update state :current-letter inc)})

(defmethod control :change-language [event args state]
  {:state (dec state)})

(defn state [r]
  (citrus/subscription r [:app]))

(defn letter [r]
  (print (citrus/subscription r [:app]))
  (let [current-letter-index (citrus/subscription r [:app :current-letter])
        current-letter (nth (citrus/subscription r [:letters-list]) current-letter-index)]
    (citrus/subscription r [:letters current-letter])))