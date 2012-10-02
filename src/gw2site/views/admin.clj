(ns gw2site.views.admin
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
        hiccup.form-helpers)
  (:require [noir.session :as session]
            [noir.validation :as vali]
            [noir.response :as resp]
            [clojure.string :as string]
            [gw2site.models.post :as posts]
            [gw2site.models.user :as users]
            [gw2site.views.common :as common]))

;; Actions

(def user-actions [{:url "/admin/user/add" :text "Add a user"}])
(def post-actions [{:url "/admin/post/add" :text "Add a post"}])

;; Partials

(defpartial error-text [errors]
            [:span (string/join "" errors)])

(defpartial user-fields [{:keys [username] :as usr}]
            (vali/on-error :username error-text)
            (text-field {:placeholder "Username"} :username username)
            (vali/on-error :password error-text)
            (password-field {:placeholder "Password"} :password))

(defpartial post-fields [{:keys [title body]}]
            (vali/on-error :title error-text)
            (text-field {:placeholder "Title"} :title title)
            (vali/on-error :body error-text)
            (text-area {:placeholder "Body"} :body body))

(defpartial post-item [{:keys [title] :as post}]
            [:li
             (link-to (posts/edit-url post) title)])

(defpartial action-item [{:keys [url text]}]
            [:li
             (link-to url text)])

(defpartial user-item [{:keys [username]}]
            [:li
             (link-to (str "/admin/user/edit/" username) username)])

;; Admin pages

(pre-route "/admin*" {}
           (when-not (users/admin?)
             (resp/redirect "/login")))

(defpage "/login" {:as user}
         (if (users/admin?) 
           (resp/redirect "/admin")
           (common/main-layout
             (form-to [:post "/login"]
                      [:ul.actions
                       [:li (link-to {:class "submit"} "/" "Login")]]
                      (user-fields user)
                      (submit-button {:class "submit"} "submit")))))

(defpage [:post "/login"] {:as user}
         (if (users/login! user)
           (resp/redirect "/admin")
           (render "/login" user)))

(defpage "/admin/logout" {}
         (session/clear!)
         (resp/redirect "/"))

;; Post pages

(defpage "/admin" {}
         (common/admin-layout
           [:ul.actions
            (map action-item post-actions)]
           [:br]
           [:ul.items 
            (map post-item (posts/get-latest))]))

(defpage "/admin/post/add" {:as post}
         (common/admin-layout
           (form-to [:post "/admin/post/add"]
                    [:ul.actions
                     [:li (link-to {:class "submit"} "/" "Add")]]
                    (post-fields post)
                    (submit-button {:class "submit"} "add post"))))

(defpage [:post "/admin/post/add"] {:as post}
         (if (posts/add! post)
           (resp/redirect "/admin")
           (render "/admin/post/add" post)))

(defpage "/admin/post/edit/:id" {:keys [id]}
         (if-let [post (posts/id->post id)]
           (common/admin-layout
             (form-to [:post (str "/admin/post/edit" id)]
                      [:ul.actions
                       [:li (link-to {:class "submit"} "/" "Submit")]
                       [:li (link-to {:class "delete"} (str "/admin/post/remove/" id) "Remove")]]
                      (post-fields post)
                      (submit-button {:class "submit"} "submit")))))

(defpage [:post "/admin/post/edit:id"] {:keys [id] :as post}
         (if (posts/edit! post)
           (resp/redirect "/admin")
           (render "/admin/post/edit/:id" post)))

(defpage "/admin/post/remove/:id" {:keys [id]}
         (posts/remove! id)
         (resp/redirect "/admin"))

;; User pages

(defpage "/admin/users" {}
         (common/admin-layout
           [:ul.actions
            (map action-item user-actions)]
           [:br]
           [:ul.items
            (map user-item (users/all))]))

(defpage "/admin/user/add" {:as user}
         (common/admin-layout
           (form-to [:post "/admin/user/add"]
                    [:ul.actions
                     [:li "Add"]]
                     (user-fields user)
                     (submit-button {:class "submit"} "add user"))))

(defpage [:post "/admin/user/add"] {:keys [username poassword] :as neue-user}
         (if (users/add! neue-user)
           (resp/redirect "/admin/users")
           (render "/admin/user/add" neue-user)))

(defpage "/admin/user/edit/:old-name" {:keys [old-name]}
         (let [user (users/get-username old-name)]
           (common/admin-layout
             (form-to [:post (str "/admin/user/edit/" old-name)]
                      [:ul.actions
                       [:li (link-to {:class "submit"} "/" "Submit")]
                       [:li (link-to {:class "delete"} (str "/admin/user/remove/" old-name) "Remove")]]
                      (user-fields user)))))

(defpage [:post "/admin/user/edit/:old-name"] {:keys [old-name] :as user}
         (if (users/edit! user)
           (resp/redirect "/admin/users")
           (render "/admin/user/edit/:old-name" user)))

(defpage "/admin/user/remove/:id" {:keys [id]}
         (users/remove! id)
         (resp/redirect "/admin/users"))
