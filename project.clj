(defproject clojure-spec-example "0.1.0-SNAPSHOT"
  :description "A project to explain the usage of clojure spec"
  :url "https://github.com/frogrammer/clojure-spec-example"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha10"]]
  :main ^:skip-aot clojure-spec-example.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
