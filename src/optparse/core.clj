(ns optparse.core
  (:use [clojure.string :only (replace)])
  (:refer-clojure :exclude [replace]))

(defn- create-key [string]
  (keyword (replace string "--" "")))

(defn optparse
  "Parse options, returns array with hash of named arguments, 
   array of the rest of the arguments, and usage message"
  ([args & expectations]
    (hash-map (create-key (first args)) true)))
