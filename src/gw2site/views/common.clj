(ns gw2site.views.common
    (use noir.core
         hiccup.core
         hiccup.page-helpers))

(def includes {:collapse (include-js "/js/animatedcollapse.js")
               :default (include-css "/css/screen.css")
               :blog (include-js "/js/blog.js")
               :jquery (include-js "http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js")})

(def animatedcollapse "
  <script type=\"text/javascript\">
  
  /***********************************************
   * Animated Collapsible DIV v2.4- (c) Dynamic Drive DHTML code library (www.dynamicdrive.com)
   * This notice MUST stay intact for legal use
   * Visit Dynamic Drive at http://www.dynamicdrive.com/ for this script and 100s more
   ***********************************************/

  animatedcollapse.addDiv('podcasts', 'fade=1,speed=400,group=nav,hide=1')
  animatedcollapse.addDiv('blogs', 'fade=1,speed=400,group=nav,hide=1')
  animatedcollapse.init();
    
  </script>")

(def main-header-links [{:url "/" :text "Podcasts" :link "podcasts" :ids ["Number 1", "Number 2", "More"]}
                        {:url "/" :text "Blogs" :link "blogs" :ids ["Rawdoc", "Lamarius", "Danny"]}
                        {:url "/" :text "About" :link "about"}])

(def admin-header-links [{:url "/admin" :text "Admin" :link "admin"}
                         {:url "/admin/users" :text "Users" :link "admin/users"}
                         {:url "/admin" :text "Posts" :link "admin"}
                         {:url "/admin/logout" :text "Logout" :link "admin/logout"}])

(defpartial build-head [incls]
    [:head
        [:title "OH YEAH"]
        (map #(get includes %) incls)
        animatedcollapse])

(defpartial dropdown-item [item]
    [:li
      (link-to {:class "bottomlevel"} "/lamarius" item)
    ])

(defpartial link-item [{:keys (url link ids text)}]
    [:li
        [:div 
          (if (not-empty ids) 
            (link-to {:class "toplevel"} (str "javascript:animatedcollapse.toggle('" link "')") text)
            (link-to {:class "toplevel"} (str "/" link) text)
          )
        ]
        [:div {:id link}
          [:ul 
            (map dropdown-item ids)
          ]
        ]])

(defpartial logo []
            (link-to "/" (image "/img/SmallererDinoLogo.png" "BOOM")))

(defpartial build-header [links] 
                [:div.logo.clearfix
                  (logo)
                  [:div.large                  
                    [:span.green "S"]
                    [:span "pec"]
                    [:span.green "T"]
                    [:span "act"]
                    [:span.green "FU"]
                  ]
                     
                  [:div.small "Gaming Crew"]                 
                  [:div.links
                    [:ul
                      (map link-item links)]]])
        
(defpartial build-footer []
    [:footer
     ]
  )

(defpartial main-layout [& content]
    (html5
        (build-head [:default :jquery :collapse :blog])
        [:body
          [:div#container
            [:header#sidebar
              (build-header main-header-links)
              (build-footer)
            ]
            [:section#content.clearfix
                  (println content)
                  content]
                  ]]))

(defpartial admin-layout [& content]
    (html5
        (build-head [:default :jquery :collapse :blog])
        [:body
          [:div#container
            [:header#sidebar
              (build-header admin-header-links)
              (build-footer)
            ]
            [:section#admincontent.clearfix
                  content]]]))
