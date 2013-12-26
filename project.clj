(defproject servant "0.1.3"
  :source-paths ["src/cljs"]
  :description "A Clojurescript Library for interacting with webworkers sanely"
  :url "https://github.com/MarcoPolo/Servant"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1909"] 
                 [org.clojure/core.async "0.1.242.0-44b1e3-alpha"]
                 [com.cemerick/clojurescript.test "0.0.4"] ]
  :plugins [[lein-cljsbuild "0.3.3"]]
  :cljsbuild
              {:builds
               [{:id "servant"
                 :source-paths ["src/cljs/servant"]
                 :compiler {:optimizations :advanced
                            :pretty-print false
                            :output-to "main.js"
                            :source-map "main.js.map"
                            }}
                {:id "tests"
                 :source-paths ["src" "test"]
                 :compiler {:output-to "target/cljs/testable.js"
                            :source-map "target/cljs/testable.js.map"
                            :optimizations :simple
                            :pretty-print true}} ]
               :test-commands {"tests" ["phantomjs" "target/cljs/testable.js"]}})
