(ns servant.core
  (:require 
            ;[cljs.core.async :refer [chan close! timeout]]
            ;[clojure.browser.repl :as repl]
            ;[servant.test-ns :as test-ns]
    )
  ;(:require-macros [cljs.core.async.macros :as m :refer [go]])
  )


;(defn ^:export lolz [a b] `(servant.test-ns/sweet-fn-bro ~a ~b))
(def ^:export t (clj->js {:command "function"}))

(.log js/console t)

#_(defn window-load []
  (def worker-demo (js/Worker. "/worker.js"))
  (.addEventListener 
    worker-demo
    "message" #(.log js/console (str "worker said " (.stringify js/JSON (.-data %1)))) false)

  ;(.postMessage worker-demo (clj->js {:command "function" :function-str (.toString lolz) :args [8 7]}))
  ;(repl/connect "http://localhost:43922/943/repl")
  ;(repl/connect (get-repl-endpoint))
  ;
  (+ 1 2)
  (def k (cljs.core.async/chan 3))

  #_(comment 
    (go 
      (<! (cljs.core.async/timeout 100))
      (.log js/console "loading: 42")
      (>! k 42))

    (go 
      (.log js/console "Trying to read channel")
      (.log js/console (<! k))
      (.log js/console "Channel read")
      )

    (def j (cljs.core.async/chan 3))

    (go 
      (.log js/console "Trying to read channel")
      (.log js/console (<! j))
      (.log js/console "Channel read")
      )

    (go 
      (<! (cljs.core.async/timeout 100))
      (.log js/console "loading: 42")
      (>! j 42))
    )

)


;(set! (.-onload js/window) window-load)



#_(comment 

  (require '[clojure.core.async :refer :all])

    (+ 1 2)
    (undefined? (.-worker js/self))

  (def j (chan))

    (go 
      (println "loading: 42")
      (>! j 42))

    (go 
      (println  "Trying to read channel")
      (println (<! j))
      (println "Channel read")
      )

    (go 
      (println  "Trying to read channel")
      (println (<! (timeout 100)))
      (println "Channel read")
      )


  (defservant
    channel
    (lolz 1 2))


  (in-ns 'servant.server)

  (run)

  (def repl-env (reset! cemerick.austin.repls/browser-repl-env
                        (cemerick.austin/repl-env)))

  (cemerick.austin.repls/cljs-repl repl-env)

  (cemerick.austin.repls/browser-connected-repl-js)

  (js/alert "HI")

  (defn testy [] (js/alert "hey this is a test"))

  (str "(" (.toString testy) ")()" )

  (def k (js/Blob. (str "(" (.toString testy) ")()" ) ))

  (def k (js/Blob. (clj->js [(str "(" (.toString testy) ")()" )]) (clj->js {:type "text/script"})))

  (def script (.createObjectURL js/URL k))


  (def script-tag (.createElement js/document "script"))
  (set! (.-src script-tag) script)
  *ns*
  (in-ns 'servant.core)
    *ns*

  k


  (.appendChild (.-body js/document) script-tag)

  (+ 1 2)

    servant.core

    (in-ns 'servant.core)
    (lolz 1 2)


    (.log js/console "woot")

    (.-userAgent js/navigator)

    (def t 42)
    (defn asdf [] t)

    `(~asdf)
    `(~lolz)
    `(~window-load)

  )
