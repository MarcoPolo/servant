(ns servant.repl-macro
  (:require [cemerick.austin.repls :refer (browser-connected-repl-js)])
)

;; This doesn't work, but I'd like it to
(defmacro get-repl-endpoint []
  (browser-connected-repl-js))

