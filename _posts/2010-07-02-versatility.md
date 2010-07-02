---
layout: post
title: Versatile Web Serving
---

When talking about interaction with Java libraries, most
people will think of it as a convenience thing, like in "you don't have to
reimplement SMTP in order to send emails". And while that's also true, there's
much more to it. Because some Java libraries out there are actually not boring,
but best-of-breed pieces of software that let you do things that you can hardly
do on any other platform - at least not without adding complexity to your setup.

One such library is [Jetty], and it happens to be a core part of Ringo. While
Jetty may look just like a plain servlet-compliant web server on first view, it
is actually a hybrid beast that will operate in synchronous and asynchronous
mode at the same time, in the same server.

On the flick of a switch, Jetty lets
you detach the current thread from a request and kick into asynchronous mode.
The request can then be handled by some other event, in some other thread. Or it
can just linger around and time out. All without sitting on a thread. There you
have it, instant scalable long polling and comet.

And it's very good at it. A [recent benchmark][jetty-benchmark] done by
Jetty and [CometD] authors shows how Cometd latency only rises beyond 100
milliseconds when you reach 8000 messages per second in a 20,000 client setup.
(Cometd support is available in Ringo as [a package][ringo-cometd].)

[jetty]: http://wiki.eclipse.org/Jetty/
[cometd]: http://cometd.org/
[jetty-benchmark]: http://blogs.webtide.com/gregw/entry/cometd_2_throughput_vs_latency
[ringo-cometd]: http://github.com/hns/ringo-cometd

## The Basics: JSGI

Let's look at the default synchronous case first. At its simplest, Ringo
implements [JSGI 0.3], which is basically [JSGI 0.2] with names that are more
adherent to JavaScript naming conventions.

A simple hello-world JSGI app in Ringo might look like this:

[JSGI 0.3]: http://wiki.commonjs.org/wiki/JSGI/Level0/A/Draft2
[JSGI 0.2]: http://github.com/hns/ringo-cometd

{% highlight javascript %}
function app(request) {
    return {
        status: 200,
        headers: {},
        body: ["hello world"]
    };
}

require("ringo/httpserver").Server({
    app: app,
    port: 4040
}).start();
{% endhighlight %}

What we do here is define a function that returns a JSGI response, and then
start a [web server][ringo/httpserver] on port `4040` that uses this function as its only handler.

[ringo/httpserver]: http://ringojs.org/api/master/ringo/httpserver

So far so good. This is not really exciting, but it's were you would start
when implementing your own web framework on top of Ringo.

(Sidenote: Ringo is _not_ a monolithic full-stack web framework. While it includes
web and persistence framework features, you are free and even welcome to ignore
them and use Ringo as a plain JavaScript engine. We're currently considering
swapping out features into their own packages to fix this misconception.
After all, it seems many people insist on inventing their own wheels.)

## Kicking into Asynchronous Mode

To handle a HTTP request asynchronously, all we have to do is return a promise.
Ringo implements the CommonJS [Promise/A][promise api] proposal, so you could in
theory return any object that implements a `then` method, but it's much more
convenient to just use the [ringo/promise] module.

[promise api]: http://wiki.commonjs.org/wiki/Promises/A
[ringo/promise]: http://ringojs.org/api/master/ringo/promise
[ringo/scheduler]: http://ringojs.org/api/master/ringo/scheduler

We're also going to use the `setTimeout` function from [ringo/scheduler]
to resolve the promise to a JSGI response with a two second delay.
Here's the code:

{% highlight javascript %}
var setTimeout = require("ringo/scheduler").setTimeout;
var defer = require("ringo/promise").defer;

function app(request) {
    var response = defer();
    setTimeout(function() {
        response.resolve({
            status: 200,
            headers: {},
            body: ["hello world"]
        });
    }, 2000);
    return response;
}

require("ringo/httpserver").Server({
    app: app,
    port: 4040
}).start();
{% endhighlight %}

So we've seen how to do basic synchronous and asynchronous request
handling. Let's see how to put these together to do something potentially useful.

## Ping and Pong

The following is an example that mixes sync/async in the same JSGI handler.

If the app recieves a request without a query parameter named `msg`, it returns
a promise, thus detaching the thread from the request while keeping the
connection open. The promise is registered in an array. If the app receives
a request with a `msg` query string parameter, its value is broadcasted to all
waiting connections.

This is how it's implemented:

{% highlight javascript %}
var defer = require("ringo/promise").defer;
var Request = require("ringo/webapp/request").Request;

var listeners = [];

function app(request) {
    if (Request(request).params.msg) {
        listeners.forEach(function(listener) {
            listener.resolve({
                status: 200,
                headers: {},
                body: [request.params.msg]
            });
        });
        listeners.length = 0;
        return {status: 200, headers: {}, body: ["done"]};
    } else {
        var response = defer();
        listeners.push(response);
        return response;
    }
}

require("ringo/httpserver").Server({
    app: app,
    port: 4040
}).start();
{% endhighlight %}

One thing that may need explanation here is the use of the `Request` function
imported from [ringo/webapp/request]. The JSGI request by itself
only provides minimal functionality. The Request function takes a JSGI request
and adds some useful features to it, such as support for parameter and file
upload parsing, cookie handling, and session support.

Previously (with JSGI 0.2), `Request` was an actual constructor that returned a
wrapper around the JSGI request object. Since we switched to JSGI 0.3,
it just enhances the existing JSGI request, which explains its potentially
confusing naming and usage.

[ringo/webapp/request]: http://ringojs.org/api/master/ringo/webapp/request

I hope you enjoyed this installment on Ringo web serving. If you did consider
subscribing to the [news feed](/atom.xml) for future updates!

