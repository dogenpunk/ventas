(ns ventas.themes.clothing.pages.frontend.profile.account
  (:require
   [ventas.i18n :refer [i18n]]
   [ventas.routes :as routes]
   [ventas.themes.clothing.components.skeleton :refer [skeleton]]
   [re-frame.core :as rf]))

(defn page []
  [skeleton
   [:div.login-page
    (let [session @(rf/subscribe [:ventas/db [:session]])]
      [:div "nothing"])]])

(routes/define-route!
 :frontend.profile.account
 {:name (i18n ::page)
  :url ["account"]
  :component page})