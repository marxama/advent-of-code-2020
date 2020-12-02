(ns advent-of-code-2020.day01
  (:require [advent-of-code-2020.utils :as utils]))

;; Wanted to have some fun with this one, and not go straight for the brute-foce
;; check-all-combinations solution.
;; Checking all combinations (for part 2) would be basically O(C(n, 3)), whereas
;; I think this solution is something like O(n + C(n, 2)) = O(C(n, 2)). I think...

(defn get-input []
  (utils/read-input-list-of-ints "day01"))

;; TODO: Make this more generic if necessary at some other point
(defn combinations-of-2 [coll]
  (mapcat
   (fn [n index] (map #(vector n %) (drop (inc index) coll)))
   coll
   (range)))

(defn get-pairs-by-sum
  "Given a collection of numbers, returns a map from int, to a collection of
   pairs of numbers adding up to that sum."
  [nums]
  (group-by (partial apply +) (combinations-of-2 nums)))

(defn pairs-summing-to [sum nums]
  (let [nums (set nums)]
    (->> nums
         (map #(hash-set % (nums (- sum %))))
         distinct
         (filter (partial every? some?)))))

(defn solve-part-1 []
  (-> (get-input)
      get-pairs-by-sum
      (get 2020)
      (->> (map (partial apply *)))))

(defn solve-part-2 []
  (let [nums (get-input)
        pairs-by-sum (get-pairs-by-sum nums)]
    (->> nums
         (mapcat (fn [n] (map (partial cons n) (pairs-by-sum (- 2020 n)))))
         (map (partial apply *))
         distinct)))
