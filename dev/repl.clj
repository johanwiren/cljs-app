(ns repl
  (:require
   [cljsapp.backend.server :as server]
   [ring.adapter.jetty :as jetty]
   [shadow.cljs.devtools.api :as shadow]))

(defonce jetty-ref (atom nil))

(defn start
  {:shadow/requires-server true}
  []

  (reset! jetty-ref
    (jetty/run-jetty #'server/handler
      {:port 3000
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
