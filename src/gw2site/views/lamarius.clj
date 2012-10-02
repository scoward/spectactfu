(ns gw2site.views.lamarius
    (:require [gw2site.views.common :as common])
    (use noir.core
         hiccup.core
         hiccup.page-helpers))

(defpage "/lamarius" []
    (common/main-layout
              [:div#news.clearfix
                  [:div.article
                      [:h2.title "Some dooshy shit"]
                      [:div.content
                          [:div.credits "August 20, 2012 at 11:22 pm | Posted by Rawdoc"]
                          [:p.special "STFU will be rolling on the realm:"]
                          [:span.large "NORTHERN SHIVERPEAKS"]
                          [:p "Please don't fail at choosing the correct server, you nitwits."]
                          [:h3 "...in unrelated news"]
                          [:iframe.center {:width 560 :height 315 :src "http://youtube.com/embed/9bZkp7q19f0" :frameborder 0}]
                        ]]
               ]
        ))
