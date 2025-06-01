(ns cljsapp.backend.server
  (:require [ring.adapter.jetty :as jetty]
            [reitit.ring :as ring])
  (:gen-class))

(def routes
  [["/api/hello" (constantly {:status 200 :body "Hello from backend"})]
   ["/*" (ring/create-resource-handler)]])

(def handler
  (ring/ring-handler
   (ring/router routes {:conflicts (constantly nil)})
   (ring/create-default-handler)))

(defn -main [& args]
  (let [port 8080]
    (println "Starting server on port" port)
    (jetty/run-jetty handler
                     {:port port
                      :join? true})))
