(ns deploy-test
  (:require [deploy :refer :all]
            [clojure.test :refer :all]))

(defn create-config
  ([]
   (create-config {}))
  ([state]
   (merge-with into {:build-server "server"
                     :build-url "/url"
                     :servers {:staging "staging-server"}}
                    state)))

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
           {:url "server/url"}))))

(deftest get-target-branch-test
  (testing "Returns a target branch string when given config"
    (is (= (get-target-branch (create-config) "staging")
           {:params {:targetBranch "staging-server"}}))))

(deftest format-request-test
  (testing "Should return a map of request data"
    (is (= (format-request (create-config {:url "test-url" :params {:migrate true :force true}}))
           {:url "test-url"
            :options {:form-params {:migrate true :force true}
                      :content-type :json
                      :as :json}}))))

(deftest request-deploy-test
  (testing "Should return a response map"
    (is (= (request-deploy fake-post {:url "test-url"
                                      :options {:form-params {:migrate true :force true}
                                                :content-type :json
                                                :as :json}})
           {:url "test-url"
            :options {:form-params {:migrate true :force true}
                      :content-type :json
                      :as :json}
            :body {:status :ok}}))))

(deftest merge-using-test
  (testing "Should return a map with merge-fn results merged in."
    (is (= (merge-using (fn [_] {:hello "world"}) {:key "value"})
           {:key "value"
            :hello "world"}))))

(deftest parse-response-test
  (testing "Should return the response body map"
    (is (= (parse-response {:body {:hello "world"}})
           {:hello "world"}))))

(run-tests)
