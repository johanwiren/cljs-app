(ns cljsapp.frontend.app
  (:require [replicant.dom :as rdom]))

(defonce app-element (.getElementById js/document "app"))

(defn init []
  (rdom/render app-element [:div "Hello from frontend"])
  (-> (js/fetch "/api/hello")
      (.then #(.text %))
      (.then #(js/console.log %))))
