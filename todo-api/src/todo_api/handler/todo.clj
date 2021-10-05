(ns todo-api.handler.todo
  (:require [ataraxy.core :as ataraxy]
            [ataraxy.response :as response]
            [integrant.core :as ig]))


(def todos
  (atom {1 {:id 1
            :task "aaa"}
         2 {:id 2
            :task "bbb"}
         3 {:id 3
            :task "ccc"}}))

(defmethod ig/init-key ::list-todos [_ {:keys [db]}]
  (fn [_]
    [::response/ok (vals @todos)]))

(defmethod ig/init-key ::create-todo [_ {:keys [db]}]
  (fn [{[_ todo] :ataraxy/result}]
    (let [todo-id (->> @todos
                       keys
                       (apply max)
                       inc)]
      (swap! todos assoc todo-id (merge {:id todo-id}
                                        (select-keys todo [:task])))
      [::response/created (str "/todos/" todo-id) (get @todos todo-id)])))

(defmethod ig/init-key ::fetch-todo [_ {:keys [db]}]
  (fn [{[_ todo-id] :ataraxy/result}]
    (if-let [todo (get @todos todo-id)]
      [::response/ok todo]
      [::response/not-found {:message (str "todo " todo-id " does not exist")}])))


(defmethod ig/init-key ::delete-todo [_ {:keys [db]}]
  (fn [{[_ todo-id] :ataraxy/result}]
    (if (get @todos todo-id)
      (do (swap! todos dissoc todo-id)
          [::response/no-content])
      [::response/not-found {:message (str "Todo " todo-id "does not exist")}])))

(defmethod ig/init-key ::update-todo [_ {:keys [db]}]
  (fn [{[_ todo-id todo] :ataraxy/result}]
    (clojure.pprint/pprint todo)
    (swap! todos assoc todo-id (merge {:id todo-id}
                                      (select-keys todo [:task])))
    [::response/created (str "/todos/" todo-id) (get @todos todo-id)]))
