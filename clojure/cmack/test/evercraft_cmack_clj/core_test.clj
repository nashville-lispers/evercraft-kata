(ns evercraft-cmack-clj.core-test
  (:use clojure.test
        evercraft-cmack-clj.core))

(deftest character-init
  (let [me (make-character :name "charlie")]
    (testing "Character creation"
      ;; (is (instance? character me) "character creation")
      (is (contains? me :name) "has a name")
      (is (= (:name me) "charlie") "get character name")
      (is (= (:armor-class me) 10) "default armor class")
      (is (= (:hit-points me) 5) "default hit points"))
    (testing "Character name set"
      (let [me (assoc me :name "not charlie")]
        (is (= (:name me) "not charlie") "set character name")))))

(deftest character-alignment
  (let [me (make-character :name "charlie" :alignment :Good)]
    (testing "Alignment init"
      (is (contains? me :alignment) "has alignment")
      (is (contains? #{:Good :Evil :Neutral} (:alignment me))))
    (testing "Alignment set"
      (let [me (assoc me :alignment :Evil)]
        (is (= (:alignment me) :Evil) "set alignment")))))

(deftest character-abilities
  (let [me (make-character :name "charlie")]
    (testing "Default values"
      (is (contains? me :abilities) "has abilities")
      (is (every? #{10} (vals (:abilities me))) "default ability to 10"))))

(deftest character-attack
  (let [attacker (make-character :name "attack")
        defender (make-character :name "defend")]
    (testing "Critical Hit"
      (is (= 3 (:hit-points (attack attacker defender 20))) "critical hit damage")
      (let [defender (assoc defender :armor-class 100)]
        (is (hit? defender 20) "critical hit on high AC defender")
        (is (= 3 (:hit-points (attack attacker defender 20))) "crit damage on high AC defender")))
    (testing "Basic Hits"
      (is (= 4 (:hit-points (attack attacker defender 10))) "roll == AC hit")
      (is (= 4 (:hit-points (attack attacker defender 11))) "roll > AC hit"))
    (testing "Death"
      (is (dead? (damage defender 100)) "dead on overwhelming damage")
      (is (dead? (damage defender 5)) "dead on exact damage"))))
