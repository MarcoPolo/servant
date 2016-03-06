# Servant

A Clojurescript library for interacting with webworkers sanely

## Installation 

add 
[![Clojars Project](https://img.shields.io/clojars/v/servant.svg)](https://clojars.org/servant)
to your dependencies

## Usage

Web workers are a pain to use, manage, and create, but they offer the ability of spawning real threads.
This library seeks to give you the good parts, without any of the bad parts.
  
Through the magic of core.async's channels, we can abstract out a lot of the implementation details in webworkers.
And with some careful execution, we can run the web workers in a similar context.  

### Spawning Servants
We create all our web workers at once and keep them on hand in a pool of available workers.  
Any worker can run any function, so servants do not store any state.  


```clojure
(def worker-count 2) ;; how many servants we want in our servant-pool
(def worker-script "/main.js") ;; This is whatever the name of the compiled javascript will be

;; We need to make sure that only the main script will spawn the servants.
(when-not (servant/webworker?)
  ;; We keep all the servants in a buffered channel.
  (def servant-channel (servant/spawn-servants worker-count worker-script)))
```

### Defining Functions
We define functions that servants should be able to execute with `defservantfn`. It is crucial that these functions be pure.  

Under the hood, `defservantfn` creates a normal function, but it also tells the webworker to remember this function. 

```clojure
  (defservantfn some-random-fn [a b]
    (+ a b))
  ;; This can also call other functions within the scope!

  (defn make-it-funny [not-funny]
    (str "Hahahah:" not-funny))

  (defservantfn servant-with-humor [your-joke]
    (make-it-funny your-joke))
```

### Using a servant 
To make a call you need to wire up your servant by providing the 
servant-channel, a message sending fn, and your defined servant fn.
  
* Servant-channel: Allows the function to figure out to whom it should dispatch the work. 
* message-fn: Determines how the lowlevel `worker.postMessage` is called. The simplest is defined in servant/standard-message.
* servant-fn: This is the defservantfn we created earlier
* & args
  
```clojure
  (def result-channel (servant/servant-thread servant-channel servant/standard-message servant-fn 5 6))
```

This will call the servant-fn using a servant from the pool with the args 5 6.   
This will return a channel that will contain the result. Similar to how core.async's thread works.

## Caveats
Web workers have a completely separate context from the main running script, so to be able to call functions freely, 
we use the same file for the main browser thread, and web workers. But doing that comes at a cost, you have to 
prevent the webworker from running code meant for the browser.   

Our current solution is:
```clojure
(defn window-load []
  ;; your browser specific code here
  )

(if (servant/webworker?)
  (worker/bootstrap) ;; Run the setup code for the web worker
  (set! (.-onload js/window) window-load)  ;; run the browser specific code
  )
```

## Separate Worker file

Sometimes you want to have a separate cljs file for workers, and that's fine!   
Nothing in this library prevents you from that.
  
Do the same thing you did before, but now this will run in a separate (possibly smaller) js file.  
You simply need to figure out what's the best way to pass data around the main browser context, 
which is significantly easier because they share the context!

## Examples
[Simple example project](https://github.com/MarcoPolo/servant-demo)   
[Encrypt/Decrypt Project using webworkers](https://github.com/MarcoPolo/servant-crypt-demo)

# Testing

I can't seem to figure out a good way to test web workers :(, I'd love to hear some ideas!

# TODO

Write tests.

## License

Copyright © 2013 Marco Munizaga

Distributed under the Eclipse Public License, the same as Clojure.
