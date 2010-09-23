---
layout: base
title: Home
---

<div class="feedicon">
<a href="http://twitter.com/hannesw"><img src="/images/twittericon.png" width="36" height="36" class="logo"></a>
<a href="/atom.xml"><img src="/images/feedicon.png" width="34" height="34" class="logo"></a>
</div>

# Posts

<ul class="toc">
{% for page in site.posts %}
 <li>{{ page.date | date_to_long_string }}:
 <a href="{{ page.url }}">{{ page.title }}</a></li>
{% endfor %}
</ul>

