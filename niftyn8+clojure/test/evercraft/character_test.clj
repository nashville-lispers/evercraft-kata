(ns evercraft.character-test
  (:require [clojure.test :refer :all]
            [evercraft.character :refer :all]))

;; Names
(def nate {:name "Nate"})
(def jack {:name "Jack"})

;; Alignments
(def good {:alignment :good})
(def evil {:alignment :evil})
(def neutral {:alignment :neutral})

(deftest test-set-name
  (testing "sets name"
    (is (= nate (set-name "Nate" {})))))

(deftest test-set-new-name
  (testing "setting a new name"
    (is (= jack (set-name "Jack" nate)))))

(deftest test-get-name
  (testing "getting a name"
    (is (= "Nate" (get-name nate)))))

(deftest test-set-alignment
  (testing "setting alignment with a valid alignment"
    (is (= good (set-alignment :good {})))))

(deftest test-set-invalid-alignment
  (testing "setting an invalid alignment"
    (is (= {:alignment nil} (set-alignment :invalid {})))))

(deftest test-get-alignment
  (testing "getting alignment"
    (is (= :good (get-alignment good)))))