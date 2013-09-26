# Servant

A Clojurescript library for interacting with webworkers sanely

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
(if-not (servant/webworker?)
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

### Wiring a servant 
To make a call you need to wire up your servant by providing the 
servant-channel, a message sending fn, and your defined servant fn.
  
* Servant-channel: Allows the function to figure out to whom it should dispatch the work. 
* message-fn: Determines how the lowlevel `worker.postMessage` is called. The simplest is defined in servant/standard-message.
* some-random-fn: This is the defservantfn we created earlier
  
```clojure
  (def channels (servant/wire-servant servant-channel servant/standard-message some-random-fn))
```

After wiring the servant, you will get back a vector of two channels, `[ in-channel out-channel ]`.

Which leads us to our next point.

### Serving you

You have your channels for a specific function, now lets use it.

```clojure
(go 
  (>! (first channels) [10 32])
  (.log js/console "The answer to life, the universe, and everything is:")
  (<! (second channels) [10 32]))
```

And that's all there is to it! :)

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

There is no guarantee of the order of the results from out-channel when supplying multiple inputs into the same in-channel.  
You can circumvent the problem by defining your servant fn to include a tag, and returning the tag along with the result.
  
Look at encrypt-demo.cljs for an example.


## Separate Worker file

Sometimes you want to have a separate cljs file for workers, and that's fine!   
Nothing in this library prevents you from that.
  
Do the same thing you did before, but now this will run in a separate (possibly smaller) js file.  
You simply need to figure out what's the best way to pass data around the main browser context, 
which is significantly easier because they share the context!

Check the src/separate/demo.cljs




## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
