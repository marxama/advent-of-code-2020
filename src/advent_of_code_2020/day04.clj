(ns advent-of-code-2020.day04
  (:require [advent-of-code-2020.utils :as utils]
            [clojure.string :as string]))

(defn parse-passport [line]
  (->> (string/split line #" ")
       (map #(string/split % #":"))
       (map (fn [[field value]] {(keyword field) value}))
       (reduce merge)))

(defn parse-input [lines]
  (->> lines
       (partition-by string/blank?)
       (map (partial string/join " "))
       (remove string/blank?)
       (map parse-passport)))

(defn get-input []
  (parse-input (utils/read-input-list "day04")))

(defn valid-part-1? [passport]
  (= 7 (count (select-keys passport
                           [:byr :iyr :eyr :hgt :hcl :ecl :pid]))))

(defn solve-part-1 []
  (->> (get-input)
       (filter valid-part-1?)
       count))

(defn validate-int-with [f s]
  (try
    (let [n (Long/parseLong s)]
      (f n))
    (catch NumberFormatException _
      false)))

(defn string-split-at [s index]
  (when (<= 0 index (dec (count s)))
    [(subs s 0 index) (subs s index)]))

(def field-validations
  {:byr (partial validate-int-with #(<= 1920 % 2002))
   :iyr (partial validate-int-with #(<= 2010 % 2020))
   :eyr (partial validate-int-with #(<= 2020 % 2030))
   :hgt (fn [v] (let [[num unit] (string-split-at v (- (count v) 2))]
                  (case unit
                    "cm" (validate-int-with #(<= 150 % 193) num)
                    "in" (validate-int-with #(<= 59 % 76) num)
                    false)))
   :hcl (partial re-matches #"#[0-9a-f]{6}")
   :ecl #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"}
   :pid (partial re-matches #"\d{9}")})

(defn valid-field? [[key value]]
  (if-let [validation (field-validations key)]
    (validation value)
    true))

(defn valid-part-2? [passport]
  (and (valid-part-1? passport)
       (every? valid-field? passport)))

(defn solve-part-2 []
  (->> (get-input)
       (filter valid-part-2?)
       count))
