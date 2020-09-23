(ns clojure-demo.core
  (:require [clojure.string :as str]))

;;========================= CHAPTER 3 ====================================

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-part
  [part]
  {:name (str/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining
               (into final-body-parts
                     (set [part (matching-part part)])))))))

(defn better-symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (reduce #(into %1 (set [%2 (matching-part %2)]))
          []
          asym-body-parts))

(defn hit
  [asym-body-parts]
  (let [sym-parts (better-symmetrize-body-parts asym-body-parts)
        body-parts-size-sum (reduce + (map :size sym-parts))
        target (rand body-parts-size-sum)]
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur remaining (+ accumulated-size (:size (first remaining))))))))

(defn recursive-printer
  ([]
   (recursive-printer 0))
  ([iteration]
   (println "Iteration " iteration)
   (if (> iteration 3)
     (println "Goodbye!")
     (recursive-printer (inc iteration)))))

(defn add-100
  "This function add 100 to x"
  [x]
  (+ x 100))

(defn dec-maker
  "Return new function that decrement n to x"
  [n]
  (fn [x] (- x n)))

(defn mapset
  "This function work like map, but return a set collection"
  [f s]
  (into (sorted-set) (map f s)))

;;========================= CHAPTER 4 ====================================

;; Seq is an abstraction for linear data structures. 
;; Here you find abstraction functions as used 
;; like map, reduce, take, drop etc.
;; This functions work on Seq data structure and return Seq.

;;======================================
;; map
;;======================================

(defn titleize
  [topic]
  (str topic " for the Brave and True"))

(map titleize ["Hamster" "Ragnarok"])

(map titleize '("Hamster" "Ragnarok"))

(map titleize #{"Hamster" "Ragnarok"})

(map #(titleize (second %)) {:uncomfortable-thing "Winking"})

;; map can take more collections as arguments
(map vector "ABC" "ABC")

(def human-consumption [8.1 7.3 6.6 5.0])

(def critter-consumption [0.0 0.2 0.3 1.1])

(defn unify-diet-data
  [human critter]
  {:human human
   :critter critter})

(map unify-diet-data human-consumption critter-consumption)

(def sum #(reduce + %))

(def avg #(/ (sum %) (count %)))

(defn stats
  [numbers]
  (map #(% numbers) [sum count avg]))

(def identities
  [{:alias "Batman" :real "Bruce Wayne"}
   {:alias "Spider-Man" :real "Peter Parker"}
   {:alias "Santa" :real "Your mom"}
   {:alias "Easter Bunny" :real "Your dad"}])

(map :real identities)

;;======================================
;; reduce
;;======================================

(reduce (fn [new-map [key val]]
          (assoc new-map key (inc val)))
        {}
        {:max 30 :min 10})

(reduce (fn [new-map [key val]]
          (if (> val 4)
            (assoc new-map key val)
            new-map))
        {}
        {:human 4.1
         :critter 3.9})

;; Implements map function with reduce
(defn map-by-reduce
  "This function implement map standard function by reduce."
  [f coll]
  (reduce #(concat %1 [(f %2)]) '() coll))

;;=======================================
;; take, drop, take-while and drop-while
;;=======================================

(take 3 [1 2 3 4 5 6 7 8 9 10])

(drop 3 [1 2 3 4 5 6 7 8 9 10])

(def food-journal
  [{:month 1 :day 1 :human 5.3 :critter 2.3}
   {:month 1 :day 2 :human 5.1 :critter 2.0}
   {:month 2 :day 1 :human 4.9 :critter 2.1}
   {:month 2 :day 2 :human 5.0 :critter 2.5}
   {:month 3 :day 1 :human 4.2 :critter 3.3}
   {:month 3 :day 2 :human 4.0 :critter 3.8}
   {:month 4 :day 1 :human 3.7 :critter 3.9}
   {:month 4 :day 2 :human 3.7 :critter 3.6}])

;; Until February
(take-while #(< (:month %) 3) food-journal)

;; From March
(drop-while #(< (:month %) 3) food-journal)

;; February and March
(take-while #(< (:month %) 4)
            (drop-while #(< (:month %) 2) food-journal))

;;======================================
;; filter and some
;;======================================

(filter #(< (:human %) 5) food-journal)

(filter #(< (:month %) 3) food-journal)

(some #(> (:critter %) 5) food-journal)

(some #(> (:critter %) 3) food-journal)

;;======================================
;; sort and sort-by
;;======================================

(sort [3 1 2])

(sort-by count ["aaaa" "c" "bbb"])

;;======================================
;; concat
;;======================================

(concat [1 2] [3 4])

;;======================================
;; Lazy Seqs
;;======================================

(def vampire-database
  {0 {:makes-blood-puns? false, :has-pulse? true :name "McFishwich"}
   1 {:makes-blood-puns? false, :has-pulse? true :name "McMackson"}
   2 {:makes-blood-puns? true, :has-pulse? false :name "Damon Salvatore"}
   3 {:makes-blood-puns? true, :has-pulse? true :name "Mickey Mouse"}})

(defn vampire-related-details
  [social-security-number]
  (Thread/sleep 1000)
  (get vampire-database social-security-number))

(defn vampire?
  [record]
  (and (:makes-blood-puns? record)
       (not (:has-pulse? record))
       record))

(defn identify-vampire
  [social-security-numbers]
  (first (filter vampire?
                 (map vampire-related-details social-security-numbers))))

(concat (take 8 (repeat "na")) ["Batman!"])

(take 3 (repeatedly (fn [] (rand-int 10))))

(defn even-numbers
  ([] (even-numbers 0))
  ([n] (cons n (lazy-seq (even-numbers (+ n 2))))))

(take 10 (even-numbers))

;;======================================
;; Function Functions
;;======================================

;;======================================
;; apply
;;======================================

;; apply take a collection and it apply this function at all its elements

(max 0 1 2)

(apply max [0 1 2])

(defn my-into
  [target additions]
  (apply conj target additions))

;;======================================
;; partial
;;======================================

;; partial take a function and any number of arguments.
;; It then returns a new function.
;; When you call the returned function, it calls the original 
;; function with the original arguments you supplied 
;; it along with the new arguments.

(def add10 (partial + 10))

(add10 3)

(def add-missing-elements
  (partial conj ["water" "earth" "air"]))

(add-missing-elements "unobtainium" "adamantium")

(defn my-partial
  [partialized-fn & args]
  (fn [& more-args]
    (apply partialized-fn (into args more-args))))

(def add20 (my-partial + 20))

(add20 3)

;; In general, you want to use partials when you find you’re repeating the
;; same combination of function and arguments in many different contexts. 

(defn lousy-logger
  [log-level message]
  (condp = log-level
    :warn (clojure.string/lower-case message)
    :emergency (clojure.string/upper-case message)))

(def warn (partial lousy-logger :warn))

;;======================================
;; complement
;;======================================

(def not-vampire? (complement vampire?))

(defn identify-human
  [social-security-numbers]
  (filter not-vampire?
          (map vampire-related-details social-security-numbers)))

;;================================================
;; A Vampire Data Analysis Program for the FW PD
;;================================================

(def filename "resources/suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def convertions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get convertions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  [minimum-filter records]
  (filter #(>= (:glitter-index %) minimum-filter) records))

;;======================================
;; Exercises
;;======================================

;; 1) Turn the result of your glitter filter into a list of names.
(defn glitter-filter-names
  [minimum-filter records]
  (map #(:name %) (glitter-filter minimum-filter records)))

;; 2) Write a function, append, which will append a new suspect 
;; to your list of suspects.
(defn append
  [suspects suspect]
  (conj suspects suspect))

;; 3) Write a function, validate, which will check that :name and :glitter-index
;; are present when you append. The validate function should accept
;; two arguments: a map of keywords to validating functions, similar to
;; conversions, and the record to be validated.

(defn not-nil?
  [val] ((complement nil?) val))

(defn validate
  [validators record]
  (reduce
   (fn [is-valid vamp-key]
     (and is-valid (not-nil? (get record vamp-key)) ((get validators vamp-key) (get record vamp-key))))
   true
   vamp-keys))

;; 4) Write a function that will take your list of maps and convert it back to a
;; CSV string. You’ll need to use the clojure.string/join function.
(defn to-csv
  [folks]
  (clojure.string/join "\n" (map #(clojure.string/join "," [(:name %) (:glitter-index %)]) folks)))

;;========================= CHAPTER 5 ====================================

;; This function is recursive, 
;; but Clojure haven't tail recursion optimization (TCO)
(defn sum-rec
  ([vals] (sum-rec vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (sum-rec (rest vals) (+ (first vals) accumulating-total)))))

(defn sum-rec-tail
  ([vals] (sum-rec-tail vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (recur (rest vals) (+ (first vals) accumulating-total)))))

;;======================================
;; Function Composition
;;======================================

;; You can combine function using comp function.
;; In this example comp create new function and apply
;; * function and then inc on arguments 2 3.
((comp inc *) 2 3)

;; another example for comp use

(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})

(def c-int (comp :intelligence :attributes))

(def c-str (comp :strength :attributes))

(def c-dex (comp :dexterity :attributes))

(c-int character)
(c-str character)
(c-dex character)

;; What do you do if one of the functions you want to compose needs
;; to take more than one argument? You wrap it in an anonymous function.
(defn spell-slots
  [char]
  (int (inc (/ (c-int char) 2))))

(spell-slots character)

(def spell-slots-comp (comp int inc #(/ % 2) c-int))

;;======================================
;; Memoization
;;======================================

;; Memoization lets you take advantage of referential transparency by storing
;; the arguments passed to a function and the return value of the function.
;; That way, subsequent calls to the function with the same arguments
;; can return the result immediately. This is especially useful for functions
;; that take a lot of time to run.

(defn sleepy-identity
  "Returns the given value after 1 second"
  [x]
  (Thread/sleep 1000)
  x)

(sleepy-identity "Mr. Fantastico")

(def memo-sleepy-identity (memoize sleepy-identity))

(memo-sleepy-identity "Mr. Fantastico")

;;======================================
;; Exercises
;;======================================

;; 1) You used (comp :intelligence :attributes) to create a function that
;; returns a character’s intelligence. Create a new function, attr, that you
;; can call like (attr :intelligence) and that does the same thing.
(defn attr
  [attribute]
  (fn [x] (attribute (:attributes x))))

;; 2) Implement the comp function.
(defn my-comp
  [f g]
  (fn
    ([] (f (g)))
    ([x] (f (g x)))))

;; 3) Implement the assoc-in function. Hint: use the assoc function and
;; define its parameters as [m [k & ks] v].
(defn my-assoc-in
  [m [k & ks] v]
  (loop
   [map m
    key k
    keys ks
    val v]
    (if (empty? keys)
      (assoc map key val)
      (assoc map key (my-assoc-in (get map key {}) keys val)))))

;; 4) Look up and use the update-in function.
(update-in {:a {:b {:c 2}}} [:a :b :c] inc)

;; 5) Implement update-in.
(defn my-update-in
  [m [k & ks] f & args]
  (let
   [map m
    key k
    keys ks
    fn f
    args args]
    (if (empty? keys)
      (let
       [converter (partial fn (get map key))]
        (assoc map key (apply converter args)))
      (let
       [updater (partial my-update-in (get map key {}) keys fn)]
        (assoc map key (apply updater args))))))

(my-update-in {:a {:b {:c 2}}} [:a :b :c] inc)
