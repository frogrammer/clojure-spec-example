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
(println ::suit) ;; :clojure-spec-example.spec_intro/suit

;; prints the explanation to standard outupt:
(s/explain ::suit 42)
;; returns the failure message as a string:
(s/explain-str ::suit 42)
;; returns explantaion of failure as a map:
(s/explain-data ::suit 42)
;; spec passes, returns nil:
(s/explain ::suit :club)


;; s/and:
(s/def ::big-even (s/and int? even? #(> % 1000)))
(s/conform ::big-even 1002)
(s/conform ::big-even :foo)
(s/explain-data ::big-even :foo)
(s/explain-data ::big-even 1001)

;; s/or:
(s/def ::name-or-id (s/or :name string? :id int?))
(s/conform ::name-or-id "abc")
(s/conform ::name-or-id 3)
(s/explain-str ::name-or-id 3.5)
(s/explain-data ::name-or-id 3.5)
