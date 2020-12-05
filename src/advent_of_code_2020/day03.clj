(ns advent-of-code-2020.day03
  (:require [advent-of-code-2020.utils :as utils]
            [advent-of-code-2020.math :as math]))

(defn get-input []
  (utils/read-input-list-of-chars "day03"))

;; Could've used nth with cycle instead, but below is O(1) on vectors,
;; rather than O(n)
(defn nth-wrapped
  "Like nth, but only works on finite collections - if index exceeds the coll
   length, it wraps around."
  [coll index]
  (let [length (count coll)
        index (->> index
                   (iterate #(- % length))
                   (drop-while #(<= length %))
                   first)]
    (nth coll index)))

(defn tree? [c]
  (= c \#))

(defn get-path [slope]
  (iterate (partial math/vec+ slope) [0 0]))

(defn count-trees [grid [y-delta _ :as slope]]
  (let [path (get-path slope)]
    (->> (map (fn [row [_ x]] (nth-wrapped row x))
              (take-nth y-delta grid)
              path)
         (filter tree?)
         count)))

(defn solve-part-1 []
  (count-trees (get-input) [1 3]))

;; The [2 1] case feels a bit weird for me - we're in-between two tiles on
;; every second step down the slope, but we don't consider those cases. Maybe
;; the trees are just thin enough that we wouldn't hit them? Anyway, got the
;; right answer, so leaving it for now!
(defn solve-part-2 []
  (let [grid (get-input)]
    (->> [[1 1]
          [1 3]
          [1 5]
          [1 7]
          [2 1]]
         (map (partial count-trees grid))
         (apply *))))
