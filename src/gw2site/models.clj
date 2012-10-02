(ns gw2site.models
  (:require [simpledb.core :as db]
            [gw2site.models.user :as users]
            [gw2site.models.post :as posts]))

(defn initialize []
  (db/init)
  (when-not (db/get :users)
    ;; db values need to be initialized, this should only happen once
    (users/init!)
    (posts/init!)))
