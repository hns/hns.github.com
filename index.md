---
layout: base
title: Home
---

<div class="feedicon">
<a href="./atom.xml"><img src="./images/feedicon.png" width="32" height="32" class="logo"></a>
</div>

# Posts

<ul class="toc">
{% for page in site.posts %}
 <li>{{ page.date | date_to_long_string }}: 
 <a href="{{ page.url }}">{{ page.title }}</a></li>
{% endfor %}
</ul>

