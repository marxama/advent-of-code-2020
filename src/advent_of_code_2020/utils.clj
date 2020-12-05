(ns advent-of-code-2020.utils
  (:require [clojure.string :as string]))

(defn read-input-list [f]
  (->> f (str "resources/") slurp string/split-lines))

(defn read-input-list-of-ints [f]
  (->> f read-input-list (map #(Long/parseLong %))))

(defn read-input-list-of-chars [f]
  (->> f read-input-list (map vec)))
