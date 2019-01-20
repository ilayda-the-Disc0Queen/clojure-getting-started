(defproject clojure-getting-started "1.0.0-SNAPSHOT"
  :description "Demo Clojure web app"
  :url "http://clojure-getting-started.herokuapp.com"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [environ "1.1.0"]
                 [hiccup "1.0.5"]
                 [org.clojure/data.csv "0.1.4"]]
  :min-lein-version "2.7.2"
  :plugins [[lein-environ "1.1.0"]
            [lein-ring "0.12.4"]
            [nrepl/lein-nrepl "0.3.2"]]
  :ring {:handler clojure-getting-started.web/app}
  :uberjar-name "clojure-getting-started-standalone.jar"
  :profiles {:production {:env {:production true}}})
