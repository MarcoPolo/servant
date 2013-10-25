(ns servant.macros)

(defmacro defservantfn 
  " Create the function,
    Register the function in a worker-fn map"
  [fn-sym args & body]
  `(do
     (defn ~fn-sym ~args ~@body)
     (servant.worker/register-servant-fn ~(name fn-sym) ~fn-sym)))
