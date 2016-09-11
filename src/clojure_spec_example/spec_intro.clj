(ns clojure-spec-example.spec_intro
  (require [clojure.spec :as s]
           [clojure.spec.test :as stest]))


;;; specs with predicates: returns the value if it conforms to
;;; a spec and a special keyword :clojure.spec/invalid if it doesn't
(s/conform even? 1000)
(s/conform even? 999)

;; test predicates: the returned value is a boolean
(s/valid? #(> % 5) 10)
(s/valid? #(> % 5) 0)

;; test set membership
(s/valid? #{:club :diamond :heart :spade} :club)
(s/valid? #{:club :diamond :heart :spade} 42)

;; register specs in fully qualified namespaces
(s/def ::suit #{:club :diamond :heart :spade})

(s/explain ::suit 42)
(s/explain-str ::suit 42)
(s/explain-data ::suit 42)
