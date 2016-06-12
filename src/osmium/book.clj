(ns osmium.book
  (:require [datomic.api :as d]))

(def book-keys [:db/id :book/iban :book/title :book/author :book/description])

(defn ->map [e]
  (into {:db/id (:db/id e)} e))

(defn by-id [db id]
  (->map (d/entity (d/db (:conn db)) id)))

(defn by-iban [db iban]
  (->map (d/entity (d/db (:conn db)) [:book/iban iban])))

(defn all [db]
  (mapv (comp (partial by-id db) first)
        (d/q '[:find ?b :in $ :where [?b :book/iban _]] (d/db (:conn db)))))


;;  ======================================================================
;; Write

(defn create-book! [db book]
  (d/transact (:conn db) [(assoc book :db/id #db/id[:db.part/user])]))

(defn update-description! [db id description]
  (d/transact (:conn db) [{:db/id id :book/description description}]))

(defn update-rating! [db id rating]
  (d/transact (:conn db) [{:db/id id :book/rating rating}]))
