(ns clojure-spec-example.core
  (:gen-class))

(defn -main
  "main function"
  [& args]
  (load-file "src/clojure_spec_example/spec_errors.clj"))
