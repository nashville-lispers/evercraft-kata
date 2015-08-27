(ns evercraft.character-test
  (:require [clojure.test :refer :all]
            [evercraft.character :refer :all]))

(deftest test-make-character
   (testing "character gets created with default ac"
     (is (= 10 (:ac (make-character))))))

(deftest test-make-character
  (testing "character gets created with default hp"
    (is (= 5 (:hp (make-character))))))

