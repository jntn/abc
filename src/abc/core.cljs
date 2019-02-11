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

(defn timeout [reconciler controller-name effect]
  (let [{:keys [delay on-timeout]} effect]
    (js/setTimeout #(citrus/dispatch! reconciler controller-name on-timeout) delay)))

(defonce reconciler
  (citrus/reconciler
   {:state
    (atom {})
    :controllers
    {:app db/control}
    :effect-handlers
    {:local-storage local-storage
     :timeout timeout}}))

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

