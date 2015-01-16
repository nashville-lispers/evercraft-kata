(ns evercraft.combat-test
  (:require [clojure.test :refer :all]
            [evercraft.combat :refer :all]))

(def jim {:name "Jim" :ac 4 :hp 3})
(def bob {:name "Bob" :ac 4 :hp 3})

(deftest test-attacking-combatant-miss
  (testing "missing an existing combatant"
    (is (= "Jim missed Bob" (attack jim bob 0)))))

(deftest test-attacking-combatant-hit
  (testing "hitting an existing combatant"
    (let [result (attack jim bob 5)]
      (is (= (:hp result) 2)))))

(deftest test-attacking-combatant-crit
  (testing "critically attacking an existing combatant"
    (let [result (attack jim bob 20)]
      (is (= (:hp result) 1)))))