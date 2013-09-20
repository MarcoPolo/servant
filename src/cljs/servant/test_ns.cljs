(ns servant.test-ns)

(def ^:export answer-to-life (inc (int 41)))
(defn ^:export sweet-fn-bro [a b] (str "lolz:" (+ a b answer-to-life)))

