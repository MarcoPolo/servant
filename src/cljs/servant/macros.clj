(ns servant.macros)

;; Create the function,
;; Register the function in a worker-fn map
;; Post message with key value and array of arguments

(str 'test-ns/some-random-fn)

(defmacro defservantfn [name args & body]
  `(do
     (defn ~name ~args ~@body)
     (servant.worker/register-servant-fn ~name)))

(macroexpand-1 '(defservantfn get-first-bits [ab] (test-ns/get-first-32-bits ab)))
