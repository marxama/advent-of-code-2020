(ns advent-of-code-2020.day02
  (:require [advent-of-code-2020.utils :as utils]))

(defn get-input []
  (utils/read-input-list "day02"))

(defn char-at [s i]
  (when (and s (< i (.length s)))
    (.charAt s i)))

(defn parse-line [line]
  (let [[_ min max char pwd] (re-matches #"(\d+)-(\d+) (\w): (.+)" line)]
    {:min (Integer/parseInt min)
     :max (Integer/parseInt max)
     :char (char-at char 0)
     :pwd pwd}))

(defn parse-input []
  (map parse-line (get-input)))

(defn occurrences [coll x]
  (->> coll
       (filter #{x})
       count))

(defn valid-part-1? [{:keys [min max char pwd]}]
  (<= min (occurrences pwd char) max))

(defn solve-part-1 []
  (->> (parse-input)
       (filter valid-part-1?)
       count))

(defn valid-part-2? [{:keys [min max char pwd]}]
  (= 1 (occurrences [(char-at pwd (dec min))
                     (char-at pwd (dec max))]
                    char)))

(defn solve-part-2 []
  (->> (parse-input)
       (filter valid-part-2?)
       count))
