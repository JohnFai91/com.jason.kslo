---
title: Welcome to my new website
permalink: /posts/2021/05/17/welcome-to-my-new-website
author: John Fai
layout: wide
---
## Greetings
Hi there, thanks for visiting my website! Over the past few months, I've spent hundreds of hours on developing [com.jason.kslo](https://github.com/johnfai91/com.jason.kslo) for the convenience of my friends. It has been a great time developing it.

## com.jason.kslo? What is that?
For those who don't know, com.jason.kslo is an Android app developed for students in mind. As a student and teenager in the pandemic, I got bored and was determined to make an app that changes the ten years old UI of the school intranet into somthing actually usable. The configuration and layout of the school website was absolutely perfect, but the inside? Not quite. Not only that the UI is old, it is also unusable on smartphones which have small screens. I constantly moan and groan on the UI of it. After the breakout, I researched to find a way to build an app that changes a rubbish (that's what I'll call it) into something usable (I won't say it's beautiful, afterall I'm not an artist). 

## How was the development?
I scattered the Internet to find some tools that don't require coding because I thought it would be painful. Nevertheless, after I started coding, I enjoyed it. I planned that there are three states, alpha, beta and stable. As you may see, versions 1-5 (1.0.1 alpha to 1.0.5-alpha) were in alpha state, versions 6-10 (1.0.6-beta to 1.1.0-beta) were in beta state, and the releases after that were in stable state. Of course, they are not there to be named for nothing, they have real meanings. 

### Alpha
If you joined in an earlier stage in alpha state, I used webview which was slow and the UI didn't change much, because webview is basically the browser you are using. The concept were there, but the aim wasn't fulfilled.

### Beta
Moving on to beta state, everything got complicated. In case you're wondering how the details and information came from, let me tell something that may shock you, from the school website! I used a Java library called Jsoup along with an ical library called Biweekly in this state instead of webview, afterall, I am working on an app, not a website. It took me hours to figure out the origin of the information. It isn't as easy as you think how a website loads. My job was to get every single detail scrapped from the website and implement into my app. The most difficult part was the gallery, intranet, and the library. The main reason is that they used Javascript (Arggggh!). Using javascript isn't a bad idea because it allows more user interactions besides the fact that it can track your movements in milleseconds. In fact, nowadays, most wesites uses Javascript. I had to research a lot to get the pages done. As you may remember, I released a version every week but only one page has changed! Figuring out how a website is certainly not fun, but the results are rewarding. That's the whole point of coding lol.

### Stable
Beta state was relatively lighter and easier as my main focus is to fix bugs or implement lighter features such as searching and notification (working on this). 

### Most difficult part solve, what could possibly go wrong :new_moon_with_face:	?
Talking about features, the most difficult one was the locales. I mainly use English for development as it's much easier, therefire, the first few (around 4) versions were completely in Chinese. By the time I had to translate them, I was so frustrated that I spent my whole day sitting in front of the computer and think of how to translate them back to Chinese. Now I understand why billinguals need to work harder and how translating looks like, it isn't a really great time transalting...

## Summary
It has been a few months since I first posted my whole project on Github (1.0.4 alpha was my first commit), and now that I have my website running, I would like to thank everyone especially Bosco for spotting out the issues, bugs and provide some thoughts on my app. Sorry in advance if you ever think to be forced to join :rofl:.
