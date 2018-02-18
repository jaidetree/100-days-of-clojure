(ns deploy-test
  (:require [deploy :refer :all]
            [clojure.test :refer :all]))

(defn create-config
  []
  {:build-server "server"
   :build-url "/url"
   :servers {:staging "staging-server"}})

(defn fake-post
  [url options]
  {:url url
   :options options
   :body {:status :ok}})

(deftest arg->keyword-test
  (testing "Can convert a CLI --arg to :arg"
    (is (= (arg->keyword "--arg") :arg))))

(deftest parse-args-test
  (testing "Transforms a list of accepted CLI args to keywords"
    (is (= (parse-args ["--migrate" "--deploy"])
           {:migrate true :deploy true}))))

(deftest get-build-url-test
  (testing "Returns a URL string"
    (is (= (get-build-url (create-config))
           "server/url"))))

(deftest get-target-branch-test
  (testing "Returns a target branch string when given config"
    (is (= (get-target-branch
            (create-config)
            :staging)
           "staging-server"))))

(deftest create-deploy-request-test
  (testing "Should return a response body"
    (is (= (create-deploy-request fake-post :staging {:migrate true} (create-config)))
        {:status :ok})))

(run-tests)
