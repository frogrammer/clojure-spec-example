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

;; Collections


;; s/coll-of is used for vectors, lists, and sets of the same kind of elements:
(s/conform (s/coll-of keyword?) [:a :b :c])
(s/conform (s/coll-of keyword?) [:a :b "c"])
(s/explain-str (s/coll-of keyword?) [:a :b "c"])

;; s/map-of is used for maps of the same key-value types:
(s/conform (s/map-of keyword? int?) {:a 1 :b 2})
(s/conform (s/map-of keyword? int?) {:a "1" :b "2"})
(s/explain-str (s/map-of keyword? int?) {:a "1" :b "2"})

;; s/tuple works for fixed size tuples:
(s/conform (s/tuple double? double? double?) [3.0 3.5 6.12])
(s/conform (s/tuple double? double? double?) [3.0 3.5])
(s/explain-str  (s/tuple double? double? double?) [3.0 3.5])

;; Sequences


;; Concatenation s/cat:
(s/def ::ingredient (s/cat :quantity number? :unit keyword?))
(s/conform ::ingredient [2 :teaspoon])
(s/explain-str ::ingredient [:to-taste :salt])

;; Alternatives s/alt:
(s/def ::even-or-small (s/alt :even even? :small #(< % 42)))
(s/conform ::even-or-small [100])
(s/conform ::even-or-small [21])
(s/conform ::even-or-small [10])
(s/explain-str ::even-or-small [99])

;; s/* matches 0 or more occurrences of a pattern:
(s/def ::seq-of-keywords (s/* keyword?))
(s/conform ::seq-of-keywords [:a :b :c])
(s/conform ::seq-of-keywords [])
(s/explain-str ::seq-of-keywords [:a "b" :c 6])

;; s/+ matches 1 or more occurrences of a pattern:
(s/conform (s/+ keyword?) [:a :b :c]) ;; returns [:a :b :c]
(s/conform (s/+ keyword?) [])
(s/explain-str (s/+ keyword?) [])


;;s/? matches 0 or 1 occurrences of a pattern in a sequence, returns the matched value or nil:
(s/conform (s/? keyword?) [:hi])
(s/conform (s/? keyword?) [])
(s/conform (s/? keyword?) [:hi :bye])
(s/explain-str (s/? keyword?) [:hi :bye])

;; Combined regexes:
(s/def ::odds-then-maybe-even (s/cat :odds (s/+ odd?) :even (s/? even?)))
(s/conform ::odds-then-maybe-even [1 3 5 100])
(s/conform ::odds-then-maybe-even [1])
(s/explain-str ::odds-then-maybe-even [2])
(s/explain-str ::odds-then-maybe-even [1 2 3])

;; Using s/& for additional constraints:
(s/def ::same-evens-odds (s/& (s/* int?) #(= (count (filter odd? %)) (count (filter even? %)))))
(s/conform ::same-evens-odds [2 3 5 4])
(s/explain-str ::same-evens-odds [2 :hi 3])
(s/explain-str ::same-evens-odds [2 3 4])


(s/def ::combined-spec (s/and (s/cat :vec vector? :num number?)
                                                  #(< (count (:vec %)) (:num %))))
(s/conform ::combined-spec [[7 8] 3])


;; Functions
(defn ranged-rand
  [start end]
  (+ start (long (rand (- end start)))))

(s/fdef ranged-rand
        :args (s/and (s/cat :start int? :end int?)
                     #(< (:start %) (:end %)))
        :ret int?
        :fn (s/and #(>= (:ret %) (-> % :args :start))
                   #(< (:ret %) (-> % :args :end))))

(stest/instrument `ranged-rand)
;(ranged-rand 8 5)

;(stest/unstrument `ranged-rand)

(stest/check `ranged-rand)

;; s/nilable and use of any?
(defn add-front
  [x v]
  (into [x] v))

(s/fdef add-front
        :args (s/cat :elem any? :vec (s/nilable vector?))
        :ret (s/and vector? #(> (count %) 0))
        :fn #(= (first (:ret %)) (:elem (:args %))))
