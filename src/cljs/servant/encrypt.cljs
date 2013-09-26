(ns servant.encrypt
  (:require 
            [cljs.core.async :refer [chan close! timeout put!]]
            [servant.test-ns :as test-ns]
            [servant.core :as servant]
            [servant.worker :as worker])
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :refer [defservantfn]]) )


(def worker-count 2)
(def worker-script "/main.js") ;; This is whatever the name of this script will be

;; We will create a program that will read a file and encrypt the file using webworkers


