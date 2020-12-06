(ns advent-of-code-2020.day05
  (:require [advent-of-code-2020.utils :as utils]))

(defn get-input []
  (utils/read-input-list "day05"))

(defn parse-binary [chars]
  (let [binary (->> chars
                    (map {\F \0 \B \1 \R 1 \L 0})
                    (apply str))]
    (Integer/parseInt binary 2)))

(defn determine-row [boarding-pass]
  (parse-binary (take 7 boarding-pass)))

(defn determine-column [boarding-pass]
  (parse-binary (drop 7 boarding-pass)))

(defn parse-boarding-pass [boarding-pass]
  (let [row (determine-row boarding-pass)
        column (determine-column boarding-pass)]
    {:row row
     :column column
     :seat-id (+ column (* row 8))}))

(defn solve-part-1 []
  (->> (get-input)
       (map parse-boarding-pass)
       (map :seat-id)
       (apply max)))

(defn solve-part-2 []
  (->> (get-input)
       (map parse-boarding-pass)
       (map :seat-id)
       sort
       (partition 2 1)
       (keep (fn [[a b]]
               (when (= 2 (- b a))
                 (inc a))))
       first))
