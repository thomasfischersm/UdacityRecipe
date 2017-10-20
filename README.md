# Introduction
This is the recipe app for the Udacity Android nanodegree course.


# IMPORTANT: GETTING STARTED
Use the RELEASE build variant. The DEBUG build variant will reset the database on app restart.


# Design Choices
## Master-Detail Implementation
The master detail pattern is implemented differently from the Udacity course approach. The
Udacity course detects the presence of a view to set the a flag to indicate the tablet mode. Then
that flag is checked with if-statements in many places. Having lots of if-statements to accomplish
one goal is a messy coding practice. Further, when a structural design decision is made, the
structure of the code (e.g. class design) should represent the structure, not if-statements
peppered all over the place. Not only is it bad design. It also make the implementation bug prone.

Another problem is that views are sometimes there and sometimes not. That requires more testing
and thinking on the part of the developer.

The chosen design approach is to create fragments for each phone "screen." Each fragment is hosted
by an activity for the phone. For the tablet implementation, a separate activity is created that
hosts two fragments.

The only decision point is when the app has to choose between which activity to start.


## Grid Span Of Recipe List/Grid
The wireframes show that the recipe grid has a span of 1 for phones and 3 for tablets. The intended
implementation seems to be to create a different layout file for tablets (or different resource
value for the grid span). That design approach is obviously inferior to what is possible.

If a large phone is in landscape mode, it may make sense to use the space to show 2 columns. If a
tablet is in portrait, it may make sense to show 2 column. If there is a super huge tablet,
4 columns may make sense.

The better approach is to set a minimum width for the grid items and fit as many as possible into
the available space. I've implemented a custom layout manager and resource dimension that does that.


## How To Select A Recipe For The Widget
The project assignment was unclear on how to select the recipe that is specified by the widget. One
way would be to have the user tap the widget and choose the recipe. A nicer approach would be, if
the recipe automatically knows which recipe to display. Presumably, the last recipe that the user
looked at inside the app is the recipe the user intents to cook. (We'll assume that we don't have
multi-tasking cooks, who cook multiple recipes at the same time.)

So, the widget will ignore the normal update events and only respond to broadcast events when the
user selects a new recipe.


# Coding Standard
- I am a conscientious dissenter of prefixing field names with the letter 'm'. This app follows
the Google coding standard, not that of the Android team. There are plenty of intelligent arguments
on the Internet by leaders of the community why the prefixes are bad. If you are looking for a
pointer to get started on this topic, here you go: 
http://jakewharton.com/just-say-no-to-hungarian-notation/

- The Udacity coding standard asks for all public methods to be commented. I did NOT comment
obvious methods. For example, a 'newInstance' method on a fragment is probably going to create a
new instance of the fragment.


# Notes
- You will find some unused methods in the utility classes. I've created a few utility classes that
I use on my projects. I copy them from one project to the next. So, I didn't create them from
scratch for this one.

- I've used stackoverflow and the Android documentation quite a bit. There are individual lines of
code or short snippets copy-pasted and modified in the code all over the place. For example, I've
lifted the code to check for WiFi and adjusted it for my needs.
