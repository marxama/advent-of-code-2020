(ns advent-of-code-2020.day07
  (:require [advent-of-code-2020.utils :as utils]
            [clojure.string :as string]))

(defn get-input []
  (utils/read-input-list "day07"))

(defn parse-contents [s]
  (let [[_ amount bag] (re-matches #"(\d+) (.+) bag(s?)(\.?)" s)]
    (when (and amount bag)
      {bag (Long/parseLong amount)})))

(defn parse-rule [line]
  (let [[what contains] (string/split line #" bags contain ")
        contents (->> (string/split contains #", ")
                      (map parse-contents)
                      (reduce merge))]
    {what contents}))

(defn parse-rules [input]
  (->> input
       (map parse-rule)
       (reduce merge)))

(defn invert-rules
  "Takes a map of rules, e.g. specifying for each bag what bags it must
   contain, and inverts it, such that for each bag, you can look up what
   bags must contain it (and how many of them).
   
   For example, if the rules say
   
   {:light-red {:bright-white 1, :muted-yellow 2}}
   
   then this function will return
   
   {:bright-white {:light-red 1}
    :muted-yellow {:light-red 2}
    :light-red nil}
   
   Note that only immediate contains-relationships are included, and not
   transitive ones."
  [rules]
  (->> rules
       (map (fn [[parent children]]
              (->> children
                   (map (fn [[child count]] {child {parent count}}))
                   (reduce merge))))
       (reduce (partial merge-with merge))))

(defn contained-by [bag inverted-rules]
  (mapcat
   (fn [[parent]] (cons parent (contained-by parent inverted-rules)))
   (get inverted-rules bag)))

(defn solve-part-1 []
  (->> (get-input)
       parse-rules
       invert-rules
       (contained-by "shiny gold")
       distinct
       count))

(defn count-bags-contained-by [bag rules]
  (->> (get rules bag)
       (map (fn [[contained count]]
              (-> contained
                  (count-bags-contained-by rules)
                  inc
                  (* count))))
       (reduce +)))

(defn solve-part-2 []
  (->> (get-input)
       parse-rules
       (count-bags-contained-by "shiny gold")))
