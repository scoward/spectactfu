(ns gw2site.server
    (:require [noir.server :as server]
              [gw2site.models :as models]))

(server/load-views "src/gw2site/views/")

(defn -main [& m]
    (let [mode (or (first m) :dev) 
          port (Integer. (get (System/getenv) "PORT" "8080"))]      
        (models/initialize)
        (server/start port {:mode (keyword mode)
                            :ns 'gw2site})))
