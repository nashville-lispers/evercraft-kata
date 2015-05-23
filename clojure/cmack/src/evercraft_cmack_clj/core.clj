(ns evercraft-cmack-clj.core)

(defn clamp
  [val lower upper]
  (max (min val upper) lower))

(defrecord character
    [name alignment hit-points armor-class abilities])

(defrecord abilities
    [strength dexterity constitution wisdom intelligence charisma])

(defn ensure-alignment
  [alignment]
  (let [allowed-alignments #{:Good :Evil :Neutral}]
    (when (contains? allowed-alignments alignment)
      alignment)))

(defn truncate-map
  "remove key-values of keys not in map2"
  [map1 map2]
  (select-keys map1 (keys map2)))

(defn sanitize-ability-scores
  "Ensures ability scores are within range"
  [abilities lower upper]
  (reduce-kv #(assoc %1 %2 (clamp %3 lower upper)) {} abilities))

(defn make-abilities
  [ability-map]
  (let [default-abilities {:strength 10
                           :dexterity 10
                           :constitution 10
                           :wisdom 10
                           :intelligence 10
                           :charisma 10}
        ability-map (sanitize-ability-scores (truncate-map ability-map
                                                           default-abilities)
                                             1 20)]
    (map->abilities (merge default-abilities ability-map))))

(defn make-character
  "Create a character record"
  [& {:keys [name alignment hit-points armor-class abilities]
      :or {armor-class 10 hit-points 5}}]
  (let [alignment (ensure-alignment alignment)
        abilities (make-abilities abilities)]
   (->character name alignment hit-points armor-class abilities)))


(defn damage
  [target damage]
  (assoc target :hit-points (- (:hit-points target) damage)))

(defn critical-hit?
  [roll]
  (= roll 20))

(defn hit?
  [character roll]
  (or (>= roll (:armor-class character))
      (critical-hit? roll)))

(defn dead?
  [character]
  (<= (:hit-points character) 0))

(defn modifier
  [ability-score]
  (- (Math/floor (/ ability-score 2))
     5))

(defn attack
  [attacker target roll]
  ;; todo range check roll
  (cond
    (critical-hit? roll) (damage target 2)
    (hit? target roll) (damage target 1)
    :else target))
