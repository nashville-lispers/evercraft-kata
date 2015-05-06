(ns evercraft.character)

(defrecord GameCharacter [name alignment ac hp])

(defn make-character [& [opts]]
  (let [defaults {:ac 10 :hp 5}]
    (map->GameCharacter (merge defaults opts))))
