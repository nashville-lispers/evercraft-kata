(ns evercraft.combat)

(def default-combatant {:ac 10 :hp 5})

(defn- miss [attacker defender]
  (str (:name attacker) " missed " (:name defender)))

(defn- hit
  ([attacker defender] (hit attacker defender 1))
  ([attacker defender damage]
   (println (str (:name attacker) " hit " (:name defender)))
   (assoc defender :hp (- (:hp defender) damage))))



(defn- crit [attacker defender] (hit attacker defender 2))

(defn attack [attacker defender roll]
  (let [defending (merge default-combatant defender)]
    (cond
      (= roll 20) (crit attacker defending)
      (>= roll (:ac defender)) (hit attacker defending)
      :else (miss attacker defending))))