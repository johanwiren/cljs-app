(ns cljsapp.backend.server
  (:require [reitit.ring :as ring]))

(def routes
  [["/api/hello" (constantly {:status 200 :body "Hello from backend"})]
   ["/*" (ring/create-file-handler "./public")]])

(def handler
  (ring/ring-handler
   (ring/router routes {:conflicts (constantly nil)})
   (ring/create-default-handler)))
