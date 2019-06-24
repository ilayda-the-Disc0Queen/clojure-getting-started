(ns clojure-getting-started.web-test
  (:require [clojure.test :refer :all]
            [clojure-getting-started.web :refer :all]))

(def mock-csv [["11111" "alpha"]["11112" "beta"]])

(deftest generate-dice-roll-test
  (testing "that a dice roll is generated"
   (is (= true (some? (generate-dice-roll))))
   (is (= 5 (count (generate-dice-roll))))))
