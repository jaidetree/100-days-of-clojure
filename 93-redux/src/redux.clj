(ns redux
  (:require [clojure.pprint :refer [pprint]]))

(defn combine-reducers
  [reducers]
  (fn combined-reducer
    [state action]
    (reduce (fn combined-reducer-reducer
              [state [key reducer]]
              (assoc state key (reducer (key state) action)))
            state reducers)))

(defn create-store
  ([reducer]
   (create-store reducer {}))
  ([reducer init-state]
   (def current-state (atom init-state))
   (def listeners (atom []))

   (defn dispatch
     [action]
     (swap! current-state reducer action)
     (doseq [listener @listeners] (listener)))

   (defn subscribe
     [f]
     (swap! listeners conj f)
     (fn unsubscribe [] (swap! listeners (fn remove-listener [ls] (remove #(= % f) ls)))))

   (defn get-state
     []
     @current-state)

   {:get-state get-state
    :dispatch dispatch
    :subscribe subscribe}))

(defn users-reducer
  [state action]
  (case (:type action)
    :create-user (conj state (get action :data))
    :remove-user (remove #(= (:id %) (get-in action [:data :id])) state)
    :update-user (map #(when (= (:id %) (get-in action [:data :id]))
                             (:data action))
                      state)
    state))

(defn selected-reducer
  [state action]
  (case (:type action)
        :select-user (:data action)
        state))

(defn add-user
  [user]
  {:type :create-user :data user})

(defn remove-user
  [user]
  {:type :remove-user :data user})

(defn update-user
  [user]
  {:type :update-user :data user})

(defn select-user
  [user-id]
  {:type :select-user :data user-id})

(defn -main
  []
  (let [reducer (combine-reducers {:users users-reducer
                                   :selected selected-reducer})
        {:keys [get-state dispatch subscribe]} (create-store reducer {:users [] :selected nil})]
    (subscribe (fn subscriber [] (pprint (get-state))))
    (dispatch (add-user {:id 1 :name "Yoanna"}))
    (dispatch (add-user {:id 2 :name "Jay"}))
    (dispatch (remove-user {:id 2 :name "Jay"}))
    (dispatch (update-user {:id 1 :name "Yoanna Banana"}))
    (dispatch (select-user 1))
    (dispatch (select-user nil))))

(-main)
