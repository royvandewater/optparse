(ns optparse.core-test
  (:require [clojure.test :refer :all]
            [optparse.core :refer :all]))

(testing "handling no-argument flags"
  (deftest should-handle-true-flag
    (is (= {:help true} (optparse ["--help"] 
                                  ["-h" "--help"]))))
  (deftest should-handle-another-true-flag
    (is (= {:verbose true} (optparse ["--verbose"]
                                     ["-v" "--verbose"])))))

; (testing "handling default values for arguments not passed in"
;   (deftest should-return-default-value-when-no-flag
;     (is (= {:number 5} (optparse []
;                                  ["-n" "--number=[value]" :default 5])))))

(testing "map-keys"
  (deftest should-add-true-value-to-each-key
    (is (= {:verbose true} (map-keys ["--verbose"] 
                                     '(["-v" "--verbose"])))))

  (deftest should-add-true-value-to-each-short-form-key
    (is (= {:help true, :verbose true} (map-keys ["--help", "-v"]
                                                 '(["-h" "--help"]
                                                   ["-v" "--verbose"]))))))
