(ns abc.db
  (:require
   [citrus.core :as citrus]
   [rum.core :as rum]))

(def initial-state
  "The initial state for the application"
  {:state nil
   :current-language :sv
   :letter-index 0
   :languages {:sv {:letter-list [:a :b :c :d :e :f :g :h :i :j :k :l :m :n :o :p :q :r :s :t :u :v :x :y :z :å :ä :ö]
                    :letters {:a {:char "A" :emoji "🐵" :word "APA"}
                              :b {:char "B" :emoji "🌺" :word "BLOMMA"}
                              :c {:char "C" :emoji "🚴‍" :word "CYKEL"}
                              :d {:char "D" :emoji "💃" :word "DANS"}
                              :e {:char "E" :emoji "🐘" :word "ELEFANT"}
                              :f {:char "F" :emoji "🐠" :word "FISK"}
                              :g {:char "G" :emoji "🐸" :word "GRODA"}
                              :h {:char "H" :emoji "🐴" :word "HÄST"}
                              :i {:char "I" :emoji "🦔" :word "IGELKOTT"}
                              :j {:char "J" :emoji "🎅" :word "JULTOMTE"}
                              :k {:char "K" :emoji "🦀" :word "KRABBA"}
                              :l {:char "L" :emoji "💡" :word "LAMPA"}
                              :m {:char "M" :emoji "📱" :word "MOBIL"}
                              :n {:char "N" :emoji "🌃" :word "NATT"}
                              :o {:char "O" :emoji "🐍" :word "ORM"}
                              :p {:char "P" :emoji "🍿" :word "POPCORN"}
                              :q {:char "Q" :emoji "🇶🇦" :word "QATAR"}
                              :r {:char "R" :emoji "🚀" :word "RAKET"}
                              :s {:char "S" :emoji "🕶" :word "SOLGLASÖGON"}
                              :t {:char "T" :emoji "🚂" :word "TÅG"}
                              :u {:char "U" :emoji "🦉" :word "UGGLA"}
                              :v {:char "V" :emoji "🌊" :word "VÅG"}
                              :x {:char "X" :emoji "🙅‍" :word "X"}
                              :y {:char "Y" :emoji "🗣" :word "YLA"}
                              :z {:char "Z" :emoji "🦓" :word "ZEBRA"}
                              :å {:char "Å" :emoji "⛈" :word "ÅSKA"}
                              :ä {:char "Ä" :emoji "🍽" :word "ÄTA"}
                              :ö {:char "Ö" :emoji "🏝" :word "Ö"}}}
               :en {:letter-list [:a :b]
                    :letters {:a {:char "A" :emoji "🍏" :word "APPLE"}
                              :b {:char "B" :emoji "🐝" :word "BEE"}}}}})

(defn state [r]
  (rum/react (citrus/subscription r [:app])))

(defn get-letter-info [state]
  (let [language (:current-language state)
        letter-index (:letter-index state)
        letter (get-in state [:languages language :letter-list letter-index])
        letter-info (get-in state [:languages language :letters letter])]
    letter-info))

(defn is-last-letter [state]
  (let [language (:current-language state)
        letter-index (:letter-index state)
        letter-list (get-in state [:languages language :letter-list])]
    (= letter-index (- (count letter-list) 1))))

(defmulti control (fn [event] event))

(defmethod control :init [event args state]
  {:state initial-state})

(defmethod control :enter-letter [event args state]
  (let [letter-info (get-letter-info state)
        letter-index (:letter-index state)
        entered-char (.toUpperCase (first args))
        current-char (:char letter-info)]
    (when (not= :correct (:state state)) ; Do not accept if in transition
      (if (= entered-char current-char)
        {:timeout {:delay 700 :on-timeout :next-letter}
         :state (assoc state :state :correct)}
        {:timeout {:delay 700 :on-timeout :set-state-nil}
         :state (assoc state :state :incorrect)}))))

(defmethod control :next-letter [event args state]
  {:state
   (assoc (if (is-last-letter state)
            (assoc state :letter-index 0)
            (update state :letter-index inc)) :state nil)})

(defmethod control :set-state-nil [event args state]
  {:state
   (assoc state :state nil)})

(defmethod
  control :change-language [event args state]
  {:state (dec state)})


