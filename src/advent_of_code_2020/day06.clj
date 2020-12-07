(ns advent-of-code-2020.day06
  (:require [advent-of-code-2020.utils :as utils]
            [clojure.set :as set]))

(defn get-input []
  (utils/read-input-list-of-chars "day06"))

(defn parse-groups [input]
  (->> input
       (map set)
       (partition-by empty?)
       (remove (partial every? empty?))))

(defn count-group-part-1 [answers]
  (count (apply set/union answers)))

(defn solve-part-1 []
  (->> (get-input)
       parse-groups
       (map count-group-part-1)
       (apply +)))

(defn count-group-part-2 [answers]
  (count (apply set/intersection answers)))

(defn solve-part-2 []
  (->> (get-input)
       parse-groups
       (map count-group-part-2)
       (apply +)))
