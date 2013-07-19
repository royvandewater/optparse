(ns optparse.core
  (:use [clojure.string :only (replace)])
  (:refer-clojure :exclude [replace]))


(defn- valid-flags [expectations]
  (set 
    (flatten 
      (map 
        (fn [expectation] (list (first expectation) (second expectation))) expectations))))

(defn- expand-flag [flag expectations]
  "returns the expanded form of the flag:
    (expand-flag '-h' (['-h' '--help'])) -> '--help'"
  (second (first (filter #(= flag (first %)) expectations))))

(defn- build-key-creator [expectations]
  (fn key-creator [flag]
    (if (re-find #"--" flag)
      (keyword (replace flag "--" ""))
      (key-creator (expand-flag flag expectations)))))

(defn- build-key-checker [expectations]
  (let [flags (valid-flags expectations)]
    (fn [potential-key] 
      (contains? flags potential-key))))

(defn- map-keys [values expectations]
  (let [key-checker (build-key-checker expectations)
        key-creator (build-key-creator expectations)]
    (apply hash-map
      (flatten
        (map #(list % true)
          (map key-creator
            (filter key-checker values)))))))

(defn optparse
  "Parse options, returns array with hash of named arguments, 
   array of the rest of the arguments, and usage message"
  ([args & expectations]
    (map-keys args expectations)))
