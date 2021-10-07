(ns todo-app.views
  (:require [re-frame.core :as re-frame]
            [todo-app.subs :as subs]))

(defmulti view :handler)

(defmethod view ::home [_]
  [:div "home"])

(defmethod view ::list [_]
  [:div "todo list"])

(defmethod view ::create [_]
  [:div "create new todo"])

(defmethod view ::edit [{:keys [route-params]}]
  [:div (str "edit todo " (:id route-params))])

#_(defmethod view ::edit [_]
  [:div "edit todo"])

(defmethod view :default [_]
  [:div "404 not found"])

(defn main-panel []
  [:div "todo app"
   [view @(re-frame/subscribe [::subs/current-route])]])

