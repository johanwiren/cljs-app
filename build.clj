(ns build
  (:require [clojure.tools.build.api :as b]))

(def pkg-name "scw-docker")
;;(def version (format "1.0.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def uber-file (format "target/%s.jar" pkg-name))

;; delay to defer side effects (artifact downloads)
(def basis (delay (b/create-basis {:project "deps.edn"})))

(defn clean [_]
  (b/delete {:path "target"}))

(defn uberjar [_]
  (b/copy-dir {:src-dirs ["src" "public"]
               :target-dir class-dir})
  (b/copy-dir {:src-dirs ["public"]
               :target-dir (str class-dir "/public")})
  (b/compile-clj {:basis @basis
                  :ns-compile '[cljsapp.backend.server]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis @basis
           :main 'cljsapp.backend.server}))
