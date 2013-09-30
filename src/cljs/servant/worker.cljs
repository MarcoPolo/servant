(ns servant.worker
  (:require 
            [servant.test-ns :as test-ns]
            [cljs.core.async :refer [chan close! timeout ]])
  (:require-macros [cljs.core.async.macros :as m :refer [go]]))
 
(def worker-fn-map (atom {}))

(defn register-servant-fn [f]
  (swap! worker-fn-map assoc (hash f) f))

(defn run-function-name [message-data]
  (let [function-name (aget message-data "fn")
        f (get @worker-fn-map function-name)
        args (aget message-data "args")]
    (apply f args)))

(defn post-array-buffer 
  "In order to send back an array buffer, your function should return 
  a vector with the result as the first item and the arraybuffers to transfer as the second.
  an array of array buffers"
  [[result arraybuffers]]
  (.postMessage js/self result (clj->js arraybuffers)))

(defn decode-message [event]
  (condp = (aget (.-data event) "command")
    "channel" (.postMessage js/self (run-function-name (.-data event)))
    "channel-arraybuffer" (post-array-buffer (run-function-name (.-data event)))))  

(defn bootstrap []
  (set! (.-onmessage js/self) decode-message))
