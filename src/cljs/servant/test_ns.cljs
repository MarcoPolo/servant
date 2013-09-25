(ns servant.test-ns)

(defn get-first-32-bits [ab] 
  (let [d (js/DataView. ab)]
    (.toString (.getUint32 d 0) 16)))

