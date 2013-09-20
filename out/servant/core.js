goog.provide('servant.core');
goog.require('cljs.core');
goog.require('clojure.browser.repl');
console.log("HELLO THERE!");
(1 + 2);
servant.core.test = (function test(name){return console.log([cljs.core.str("woot")].join(''));
});
goog.exportSymbol('servant.core.test', servant.core.test);
console.log(servant.core.test.toString());
