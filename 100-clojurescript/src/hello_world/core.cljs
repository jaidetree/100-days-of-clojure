(ns hello-world.core
    (:require react-dom))

(def body (.querySelector js/document "body"))
(def e (.-createElement js/React))

(aset body "style" "backgroundColor" "#000")

(defn MyApp
  [props]
  (e "div" (clj->js {:style {:color "#fff"}})
    (e "h1" nil "Day 100")
    (e "h2" nil "Hello world, this is my first ClojureScript app")
    (e "a" (clj->js {:href "https://github.com/jayzawrotny/100-days-of-clojure" :style {:color "teal"}}) "View 100-days-of-clojure on GitHub")))

(.render js/ReactDOM
  (e MyApp nil nil)
  (.getElementById js/document "app"))
