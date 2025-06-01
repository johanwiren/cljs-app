(ns cljsapp.frontend.app
  (:require [replicant.dom :as rdom]))

(defonce app-element (.getElementById js/document "app"))

(defn init []
  (rdom/render app-element [:div "Hello, world!"]))
