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

(defn- format-flag [flag]
  "returns the flag in key form
    (expand-flag '--number=[value]') -> :number"
  (keyword (replace (replace flag "--" "") #"=.*" "")))

(defn- build-key-creator [expectations]
  (fn key-creator [flag]
    (if (re-find #"--" flag)
      (format-flag flag)
      (key-creator (expand-flag flag expectations)))))

(defn- build-key-checker [expectations]
  (let [flags (valid-flags expectations)]
    (fn [potential-key] 
      (contains? flags potential-key))))

(defn- index-of [haystack needle]
  (count (take-while #(not= needle %) haystack)))

(defn- default-value [expectation]
  (if (some #(= :default %) expectation)
    (nth expectation (+ 1 (index-of expectation :default)))))

(defn- contains-default? [expectation]
  (some #(= :default %) expectation))

(defn- extract-key [expectation]
  (format-flag (second expectation)))

(defn- generate-defaults [expectations]
  "Returns a hash with all the defaults values filled in:
    (generate-defaults (['-n' '--number' :default 5] ['-h' '--help'])) -> {:number 5}"
    (let [expectations-with-defaults (filter contains-default? expectations)
          expectation-keys           (map extract-key expectations-with-defaults)]
      (zipmap expectation-keys (map default-value expectations-with-defaults))))

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
    (let [defaults    (generate-defaults expectations)
          mapped-args (map-keys args expectations)]
      (merge defaults mapped-args))))
