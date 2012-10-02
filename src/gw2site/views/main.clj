(ns gw2site.views.main
    (:require [gw2site.views.common :as common]
              [gw2site.models.post :as posts]
              [gw2site.models.user :as user])
    (use noir.core
         hiccup.core
         hiccup.page-helpers))

(defpartial post-item [{:keys [perma-link title md-body date tme username] :as post}]
            (when post
              [:div.article
               [:h2.title (link-to perma-link title)]
                [:div.credits (str date " at " tme " | Posted by " username)]
                [:div md-body]]))

(defpartial main-page [items]
            (common/main-layout
              [:div.clearfix
               (map post-item items)]))

(defpage "/" []
         (main-page (posts/get-latest)))

(defpage "/page/:page" {:keys [page]}
         (main-page (posts/get-page page)))

(defpage "/page/" []
         (render "/page/:page" {:page 1}))

(defpage "/view/:perma" {:keys [perma]}
         (if-let [cur (posts/moniker->post perma)]
           (main-page [cur])))
