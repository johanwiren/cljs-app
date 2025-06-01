(ns repl
  (:require
   [cljsapp.backend.server :as server]
   [ring.adapter.jetty :as jetty]))

(defonce jetty-ref (atom nil))

(defn start
  []

  (reset! jetty-ref
    (jetty/run-jetty #'server/handler
      {:port 8020
       :join? false}))
  ::started)

(defn stop []
  (when-some [jetty @jetty-ref]
    (reset! jetty-ref nil)
    (.stop jetty))
  ::stopped)

(defn go []
  (stop)
  (start))

(comment

  (go)

  nil)
