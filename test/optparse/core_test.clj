(ns optparse.core-test
  (:require [clojure.test :refer :all]
            [optparse.core :refer :all]))

(testing "handling no-argument flags"
  (deftest should-handle-true-flag
    (is (= {:help true} (optparse ["--help"] 
                                  ["-h" "--help"]))))
  (deftest should-handle-another-true-flag
    (is (= {:verbose true} (optparse ["--verbose"]
                                     ["-v" "--verbose"]))))
  (deftest should-handle-short-form-flag
      (is (= {:help true} (optparse ["-h"] 
                                    ["-h" "--help"]))))
  (deftest should-handle-multiple-flags
      (is (= {:help true, :verbose true} (optparse ["--help", "-v"]
                                                   ["-h" "--help"]
                                                   ["-v" "--verbose"])))))

(testing "handling default values for arguments not passed in"
  (deftest should-return-default-value-when-no-flag
    (is (= {:number 5} (optparse []
                                 ["-n" "--number=[value]" :default 5])))))
