(ns abc.core
  (:require  [rum.core :as rum]
             [citrus.core :as citrus]
             [abc.views :as views]
             [abc.db :as db]))

(defn local-storage [reconciler controller-name effect]
  (let [{:keys [method data key on-read]} effect]
    (case method
      :set (js/localStorage.setItem (name key) data)
      :get (->> (js/localStorage.getItem (name key))
                (citrus/dispatch! reconciler controller-name on-read))
      nil)))

(defonce reconciler
  (citrus/reconciler
   {:state
    (atom {}) ;; application state
    :controllers
    {:app db/control} ;; controllers
    :effect-handlers
    {:local-storage local-storage}})) ;; effect handlers

(defn keypress [e]
  (let [key (.fromCharCode js/String (.-keyCode e))]
    (citrus/dispatch! reconciler :app :enter-letter key)))

;; initialize controllers
(defonce init-ctrl (citrus/broadcast-sync! reconciler :init))

(defn ^:export main
  []
  (set! (.-onkeypress js/document) keypress)
  (rum/mount (views/App reconciler)
             (.getElementById js/document "app")))

