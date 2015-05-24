(ns evercraft-cmack-clj.core-test
  (:use clojure.test
        evercraft-cmack-clj.core))

(deftest character-init
  (let [me (make-character :name "charlie")]
    (testing "Character creation"
      ;; (is (instance? character me) "character creation")
      (is (contains? me :name) "has a name")
      (is (= (:name me) "charlie") "get character name")
      (is (= (:armor-class me) 10) "default armor class 10")
      (is (= (:hit-points me) 5) "default hit points 5")
      (is (and (contains? me :xp) (zero? (:xp me))) "default experience points 0"))

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
      (is (every? #{10} (vals (:abilities me))) "default ability to 10"))
    (testing "Value ranges"
      (let [me (make-character :name "charlie" :abilites {:strength 30 :stupidity 90})]
        (is (not (contains? (:abilities me) :stupidity)) "invalid abilities should not exist")
        (is (every? #(<= 1 %1 20) (vals (:abilities me))) "values in range")))))

(deftest character-attack
  (let [attacker (make-character :name "attack")
        defender (make-character :name "defend")]
    (testing "Damage Calculation"
      (is (= 1 (hit-damage) (hit-damage 0)))
      (is (= (* 2 (hit-damage)) (critical-damage))
          "unmodified crit damage should be double unmodified normal damage")
      (is (= (critical-damage 5) (* 2 (hit-damage 10)))
          "critical damage is double the hit damage with double modifier"))

    (testing "Critical Hit"
      (is (= 3 (:hit-points (attack attacker defender 20))) "critical hit damage")
      (let [defender (assoc defender :armor-class 100)]
        (is (hit? defender 20) "critical hit on high AC defender")
        (is (= 3 (:hit-points (attack attacker defender 20)))
            "crit damage on high AC defender")))

    (testing "Basic Hits"
      (is (= 4 (:hit-points (attack attacker defender 10))) "roll == AC hit")
      (is (= 4 (:hit-points (attack attacker defender 11))) "roll > AC hit"))

    (testing "Death"
      (is (dead? (damage defender 100)) "dead on overwhelming damage")
      (is (dead? (damage defender 5)) "dead on exact damage"))

    (testing "Ability modifiers"
      (let [attacker (make-character :name "attack"
                                     :abilities {:strength 14
                                                 :dexterity 5
                                                 :constitution 20})]
        (is (= 10 (hit-points attacker)) "constitution modifier")
        (is (= 7 (armor-class attacker)) "dexterity modifier")
        (is (= 10 (hit-points attacker)) "constitution modifier")))))

(deftest character-leveling
  (let [level1 (make-character :name "test")
        level2 (make-character :name "test" :xp 1000)]
   (testing "Basic leveling"
     (is (= 1 (level level1)) "Level defaults to 1")
     (is (= 2 (level level2)))
     (is (= 3 (level (make-character :name "test" :xp 2000)))))

   (testing "Hit point bonuses"
     (let [con-modifier (modifier (:constitution (:abilities level2)))
           hp-delta (- (hit-points level2) (hit-points level1)) ]
       (is (= hp-delta (* (+ 5 con-modifier)))
           "Hit points increase by 5 plus constitution modifier per level")))

   (testing "Roll modifiers for even levels"
     (is (and (= (roll-level-modifier level1) 0)
              (= (roll-level-modifier level2) 1)
              (= (roll-level-modifier (make-character :name "level10" :xp 10000))
                 5))
         "Roll modified by plus 1 for every even level obtained"))))
