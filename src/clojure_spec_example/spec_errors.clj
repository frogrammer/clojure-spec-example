(ns clojure-spec-example.spec_errors
  (require [clojure.spec :as s]
           [clojure.spec.test :as stest]))



;; #########################################
;; #     helper functions/predicates       #
;; #########################################

(defn length1? [coll] (= (count coll) 1))
(s/def ::length-one length1?)



;; ##########################################
;; #    spec definitions and instruments    #
;; ##########################################

;; One possible spec for the core odd? function
(s/fdef clojure.core/odd?
  :args (s/cat :check-integer integer?))

;; Another possible spec for the core odd? function
;; to check the arity first
;; (s/fdef clojure.core/odd?
;;   :args (s/and ::length-one
;;                (s/cat :check-integer integer?)))

(stest/instrument 'clojure.core/odd?)



;; ##########################################
;; #              use of spec               #
;; ##########################################

;; 1. wrong number of arguments error
;; (odd? ) ;; `:reason "Insufficient input"` with the first spec definition
;; (odd? 1 2) ;; `:reason "Extra input"` with the first spec definition
(odd? false 1) ;; `:reason` is not provided with the first spec definition

;; 2. argument type error
;; (odd? false)
;; (odd? nil)
