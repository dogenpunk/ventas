(ns ventas.email-test
  (:require
   [clojure.test :refer [deftest is use-fixtures]]
   [postal.core :as postal]
   [ventas.email :as sut]
   [ventas.email.templates :as templates]))

(def test-configuration
  {:host "smtp.gmail.com"
   :port 443
   :user "no-reply@kazer.es"
   :pass "test"
   :from "no-reply@kazer.es"
   :ssl true})

(use-fixtures :once
              #(with-redefs [sut/get-config (constantly test-configuration)]
                 (%)))

(deftest send!
  (let [received-args (atom nil)
        message {:subject "Hey!"
                 :body "My message"}]
    (with-redefs [postal/send-message (fn [& args] (reset! received-args args))]
      (sut/send! message)
      (is (= [test-configuration
              (merge message
                     {:from (:from test-configuration)})]
             @received-args)))))

(deftest send-template!
  (let [received-args (atom nil)
        subject "Hey!"
        template "Test body"
        email "test@send-template.com"]
    (defmethod templates/template :test-template [_ _]
      {:body template
       :subject subject})
    (with-redefs [postal/send-message (fn [& args] (reset! received-args args))]
      (sut/send-template! :test-template {:user {:user/email email}})
      (is (= [test-configuration
              {:body [{:content template :type "text/html; charset=utf-8"}]
               :from (:from test-configuration)
               :subject subject
               :to email}]
             @received-args)))))
