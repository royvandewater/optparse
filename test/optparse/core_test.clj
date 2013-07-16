(ns optparse.core-test
  (:require [clojure.test :refer :all]
            [optparse.core :refer :all]))

(deftest method-exists
  (testing "optparse method exists"
    (is optparse)))
