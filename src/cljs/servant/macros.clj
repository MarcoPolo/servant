(ns servant.macros)

;; Create the function,
;; Register the function in a worker-fn map
;; Post message with key value and array of arguments

(str 'test-ns/some-random-fn)

(defmacro defservantfn [worker name args & body]
  `(let [func# (fn ~args ~@body)   
         func-str# (.toString func#)]
    (defn ~name [& arg-list#] 
      (.postMessage ~worker (clj->js {"command" "function" "function-str" func-str# "args" arg-list#})))))

