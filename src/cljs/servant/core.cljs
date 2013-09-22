(ns servant.core
  (:require 
            [cljs.core.async :refer [chan close! timeout put!]]
            [servant.test-ns :as test-ns]
            [servant.worker :as worker])
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :as sm :refer [defservantfn]])
  )

;; Let's talk about what I want to happen:

;; I want to be able to do something like
(comment 
  (def answer-to-life 42)
  (def channel (chan))
  (defn some-random-fn [a b] (+ a b))

  (def in-channel 
    (servant/serve some-random-fn) ;; returns a channel that will pass data to the web worker and run some-random-fn on it
    )

  (go 
    (>! in-channel [answer-to-life 5]))

  ;; then read from the channel
  (go 
    (.log js/console (str "Servant said: " (<! channel))))
)

(defn defservant [f]
  (swap! worker.core/worker-fn-map assoc (str f) f))

(defservant test-ns/some-random-fn)

(defn serve-worker [worker f]
  (let [in-channel  (chan)
        out-channel (chan)]

    (go 
      (loop []
        (.log js/console "Reading in-channel")
        (.log js/console "posting message" (<! in-channel))
        (recur)))

    (.addEventListener
      worker
      #(go 
         (>! out-channel (.-data %1))))

    [in-channel out-channel]))


(defn window-load []
  (def worker-demo (js/Worker. "/main.js"))
 
  (.addEventListener 
    worker-demo
    "message" #(.log js/console (str "worker said " (.stringify js/JSON (.-data %1)))) false)

  (def channels (serve-worker worker-demo test-ns/some-random-fn))
  (def in-c (first channels))

  (put! in-c [5 6])

  (go 
    (.log js/console 
      "The value from the worker is"
      (<! (second channels)))) 

  

  (def k (chan))

  (go 
    (.log js/console "Trying to read channel")
    (.log js/console (<! k))
    (.log js/console "Channel read"))

  (put! k 42 #(.log js/console "done"))


  ;(defservantfn worker-demo lolz [a b]
  ;  (+ a b))

  ;(.postMessage worker-demo (clj->js {:command "function" :function-str (.toString lolz) :args [8 7]}))
  (comment 

    (def k (chan))


    (require '[clojure.core.async :refer :all])
    (go 
      (println "Trying to read channel")
      (println (<! k))
      (println "Channel read"))

    (go 
      (.log js/console "Trying to read channel")
      (.log js/console (<! k))
      (.log js/console "Channel read"))

    (go 
      (println "loading: 42")
      (put! k 42))
  
    (+ 1 2)
    )
)


(if (undefined? (.-document js/self))
  (worker/bootstrap)
  (set! (.-onload js/window) window-load))

