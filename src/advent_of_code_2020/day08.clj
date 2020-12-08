(ns advent-of-code-2020.day08
  (:require [advent-of-code-2020.utils :as utils]))

(defn get-input []
  (utils/read-input-list "day08"))

(defn parse-instruction [line]
  (let [[instruction value] (.split line " ")]
    [(keyword instruction) (Integer/parseInt value)]))

(defn parse-program [lines]
  (mapv parse-instruction lines))

(defn initial-state [program]
  {:acc 0
   :pc 0
   :program program
   :terminated? false})

(defn step [{:keys [pc program] :as state}]
  (if-let [[instruction value] (get program pc)]
    (update (case instruction
              :nop state

              :acc (update state :acc + value)

              :jmp (update state :pc + (dec value)))
            :pc inc)
    (assoc state :terminated? true)))

(defn step-until-terminated-or-loop [state]
  (reduce
   (fn [visited-pcs {:keys [pc] :as state}]
     (if (visited-pcs pc)
       (reduced state)
       (conj visited-pcs pc)))
   #{}
   (iterate step state)))

(defn solve-part-1 []
  (->> (get-input)
       parse-program
       initial-state
       step-until-terminated-or-loop
       :acc))

(defn flip-instruction [state at-pc]
  (update state :program
          update at-pc
          update 0 #(case %
                      :jmp :nop
                      :nop :jmp
                      %)))

;; There's probably a much, much more efficient solution than this! But it
;; works, and still just takes ~50msec on my old laptop, soo let's run with
;; it for now.
(defn solve-part-2-brute-force []
  (let [program (parse-program (get-input))
        initial-state (initial-state program)]
    (->> (range (count program))
         (map (partial flip-instruction initial-state))
         (remove #{initial-state})
         (map step-until-terminated-or-loop)
         (filter :terminated?)
         first
         :acc)))
