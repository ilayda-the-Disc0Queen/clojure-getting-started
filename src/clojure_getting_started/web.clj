(ns clojure-getting-started.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [hiccup.core :as hc]
            [environ.core :refer [env]]
            [clojure.data.csv :as csv]))

(def default-file-location "./data/wordlist.csv")

(defn load-wordlist-file [path-with-extension]
  (with-open [reader (io/reader path-with-extension)]
        (doall
        (csv/read-csv reader))))

(defn generate-dice-roll []
   (->> #(rand-int 6)
        (repeatedly 5)
        (map inc)
        clojure.string/join))

(defn generate-n-dice-rolls [n]
  (repeatedly n generate-dice-roll))

(defn dice-roll->word [dice-roll csv-collection]
   (->> (filter (fn [i] (= (first i) dice-roll))
                csv-collection)
        first
        second))

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (hc/html [:h1 "Hello from Ilayda"])})

(defroutes app
  (GET "/" []
       (splash))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
