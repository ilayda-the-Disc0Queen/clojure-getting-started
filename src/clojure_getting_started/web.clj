(ns clojure-getting-started.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [ring.adapter.jetty :as jetty]
            [hiccup.core :as hc]
            [jsonista.core :as j]
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
        str/join))

(defn generate-n-dice-rolls [n]
  (repeatedly n generate-dice-roll))

(defn dice-roll->word [dice-roll csv-collection]
   (->> (filter (fn [i] (= (first i) dice-roll))
                csv-collection)
        first
        second))

(defn generate-passphrase [n path-to-csv]
   (->> (generate-n-dice-rolls n)
        (map #(dice-roll->word % (load-wordlist-file path-to-csv)))
        (str/join " ")))


(defn splash []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (hc/html [:div
     [:h1 "Your passphrase is: "]
     [:p (generate-passphrase 6 default-file-location)]])})

(defn api []
  {:status 200
    :headers {"Content-Type" "application/json"}
    :body (j/write-value-as-string {:message (generate-passphrase 6 default-file-location)})})





(defroutes app
  (GET "/" []
       (splash))
  (GET "/api" []
        (api))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
