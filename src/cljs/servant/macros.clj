(ns servant.macros)

(defmacro defservantfn 
  " Create the function,
    Register the function in a worker-fn map"
  [name args & body]
  `(do
     (defn ~name ~args ~@body)
     (servant.worker/register-servant-fn ~name)))
