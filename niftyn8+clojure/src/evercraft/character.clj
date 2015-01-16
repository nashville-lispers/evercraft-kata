(ns evercraft.character)

(defn set-name [name char]
  (assoc char :name name))

(defn get-name [char]
  (:name char))

(defn set-alignment [alignment char]
  (assoc char :alignment (alignment #{:good :evil :neutral})))

(defn get-alignment [char]
  (:alignment char))