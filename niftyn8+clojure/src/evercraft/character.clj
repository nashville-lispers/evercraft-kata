(ns evercraft.character)

(defrecord GameCharacter [name alignment ac hp])

(def valid-alignments #{:good :evil :neutral})

(defn- validate [char-map]
  (or ((get char-map :alignment :invalid) valid-alignments)
      (throw (Exception. "Not a valid alignment")))
  char-map)

(defn make-character [& [opts]]
  (let [defaults {:ac 10 :hp 5 :alignment :neutral}
        char-map (validate (merge defaults opts))]
    (map->GameCharacter char-map)))
