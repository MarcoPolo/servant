(defproject servant "0.1.0-SNAPSHOT"
  :source-paths ["src/clj" "src/cljs"]
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.async "0.1.222.0-83d0c2-alpha"]
                 [org.clojure/clojurescript "0.0-1889"] ]
  :plugins [[lein-cljsbuild "0.3.2"]]
  :profiles {:dev {:repl-options {:init-ns servant.server}
                   :plugins [[com.cemerick/austin "0.1.1"]]}}
  :cljsbuild
              {:builds
               [{:id "advanced"
                 :source-paths ["src/cljs/servant"]
                 :compiler {:optimizations :advanced
                            :pretty-print false
                            :output-to "main.js"
                            :source-map "main.js.map"
                            }}]})
