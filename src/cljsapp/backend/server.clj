(ns cljsapp.backend.server
  (:require [ring.adapter.jetty :as jetty]
            [reitit.ring :as ring])
  (:gen-class))

(defn routes [{:keys [static-handler]}]
  [["/api/hello" (constantly {:status 200 :body "Hello from backend"})]
   ["/*" (if (= :file static-handler)
           (ring/create-file-handler "public")
           (ring/create-resource-handler))]])

(defn handler [config]
  (ring/ring-handler
   (ring/router (routes config) {:conflicts (constantly nil)})
   (ring/create-default-handler)))

(defn -main [& args]
  (let [port 8080]
    (println "Starting server on port" port)
    (jetty/run-jetty (handler {})
                     {:port port
                      :join? true})))
