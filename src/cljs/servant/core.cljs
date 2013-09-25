(ns servant.core
  (:require 
            [cljs.core.async :refer [chan close! timeout put!]]
            [servant.test-ns :as test-ns]
            [servant.worker :as worker])
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :refer [defservantfn]])
                  
  )

(defn webworker? []
  (undefined? (.-document js/self)))

(def not-webworker?  (complement webworker?))

;; Let's talk about what I want to happen:

;; I want to be able to do something like

(defn spawn-servants 
  "Returns a channel that lists available workers"
  [worker-count worker-script]
  (let [servant-channel (chan worker-count)]
    (go 
      (doseq [x (range worker-count)]
        (>! servant-channel (js/Worker. worker-script))))
    servant-channel))

(defn kill-servants
  "Kills worker-count # of workers"
  [servant-channel worker-count]
  (go 
    (doseq [x (range worker-count)]
      (.terminate (<! servant-channel)))))

(defn standard-message [worker f args]
  (.postMessage worker (js-obj "command" "channel" "fn" (hash f) "args" (clj->js args))))

(defn array-buffer-message 
  "Post message by transferring context of the arraybuffers.
  The channel should be fed data like [[normal args] [arraybuffer1 arraybuffer2]].
  Tells the worker to expect to return an arraybuffer"
  [worker f args]
  (let [[args arraybuffers] args]
    (.postMessage worker (js-obj "command" "channel-arraybuffer" "fn" (hash f) "args" (clj->js args)) (clj->js arraybuffers))))

(defn array-buffer-message-standard-reply 
  "Post message by transferring context of the arraybuffers.
  The channel should be fed data like [[arg1 arg2] [arraybuffer1 arraybuffer2]].
  Tells the worker to return normal data"
  [worker f args]
  (let [[args arraybuffers] args]
    (.postMessage worker (js-obj "command" "channel" "fn" (hash f) "args" (clj->js args)) (clj->js arraybuffers))))

(defn wire-servant [servant-channel post-message-fn f]
  (let [in-channel  (chan)
        out-channel (chan)]

    (go (loop []
          (when-let [val (<! in-channel) ]
            (let [worker (<! servant-channel)]
              (post-message-fn worker f val)
              ;; Add an event listener for the worker
              (.addEventListener worker "message"
                #(go 
                   (>! out-channel (.-data %1))
                   ;; return the worker back to the servant-channel
                   (>! servant-channel worker)))

              (recur)))))

    [in-channel out-channel]))


