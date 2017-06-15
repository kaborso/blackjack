(defproject blackjack "0.1.0-SNAPSHOT"
  :description "play blackjack via tweets"
  :url "TODO"
  :license {:name "GNU Affero General Public Licence"
            :url "http://www.gnu.org/licenses/agpl-3.0.en.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.stuartsierra/component "0.3.2"]
                 [org.clojure/core.async "0.3.443"]
                 [com.taoensso/carmine "2.16.0"]
                 [twitter-api "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [ch.qos.logback/logback-classic "1.1.3"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.11"]
                                  [com.stuartsierra/component.repl "0.2.0"]]
                   :source-paths ["dev"]}})
