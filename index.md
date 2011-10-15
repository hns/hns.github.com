---
layout: base
title: Home
---

<div class="feedicon">
<a href="http://twitter.com/hannesw"><img src="/images/twittericon.png" width="36" height="36" class="logo"></a>
<a href="/atom.xml"><img src="/images/feedicon.png" width="34" height="34" class="logo"></a>
</div>

# Blog Posts

<ul class="toc">
{% for page in site.posts %}
 <li>{{ page.date | date_to_long_string }}:
 <a href="{{ page.url }}">{{ page.title }}</a></li>
{% endfor %}
</ul>

# Slides

<ul class="toc">
 <li>Wakanday 2011:
    <a href="/slides/wakanday.pdf">A fresh approach to concurrency in (server-side) JavaScript</a></li>
 <li>DevConf.ru 2011:
    <a href="/slides/devconf-ringojs.pdf">RingoJS: Server-side JavaScript on the JVM</a> (<a href="/slides/devconf-ringojs-samples.zip">code samples</a>)</li>
 <li>FOSDEM 2011:
    <a href="/slides/fosdem-js-jvm.pdf">Rhino and RingoJS: JavaScript on the JVM</a></li>
</ul>
