# Stack
[![CircleCI](https://circleci.com/gh/tylerbwong/stack/tree/master.svg?style=svg&circle-token=8fb0220b2690c822b89b65982c3da95fc26c1f71)](https://circleci.com/gh/tylerbwong/stack/tree/master)

![Stack](/art/icon.png)

Stack is an Android application that lets you browse [stackoverflow.com](https://stackoverflow.com) and other Stack Exchange sites.
It is currently under heavy development and is unstable.

The goal of this project is to provide examples for the latest Android libraries and tools. A few noteworthy examples include:

* [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) for asynchronous code
* [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection
* [Jetpack Compose](https://developer.android.com/jetpack/compose) for declarative UI code written in Kotlin (Slowly being introduced)
* [Room](https://developer.android.com/jetpack/androidx/releases/room) for local data persistence
* [Markwon](https://github.com/noties/Markwon) for rendering markdown

### Features

* View questions, answers, and comments with rich Markdown previews
* Switch to any Stack Exchange site
* Log in to vote for questions, bookmark questions, or post new questions and answers (Saving drafts supported)
* Search for questions with advanced filter controls
* View other users' profiles
* Dark mode
* Question deep linking

### Open testing

Stack is currently in open testing. You can enroll on Google Play by clicking the badge below.

<a href="https://play.google.com/apps/testing/me.tylerbwong.stack"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" height=80px /></a>

### Subprojects

Here you can also find a few useful Gradle modules as well:

* [buildSrc](./buildSrc) - A Gradle module for common build logic with a custom plugin example.
* [compose-markdown](./compose-markdown) - A Jetpack Compose Text component that can render markdown content using [intellij-markdown](https://github.com/valich/intellij-markdown) for parsing.
* [compose-preference](./compose-preference) - A set of Jetpack Compose Preference components.
* [dynamic-list-adapter](./dynamic-list-adapter) - An abstraction around `RecyclerView` built on `ListAdapter` for asynchronous list diffing.
* [dynamic-list-adapter-viewbinding](./dynamic-list-adapter-viewbinding) - View Binding support for `DynamicListAdapter`.
* [dynamic-list-adapter-extensions](./dynamic-list-adapter-extensions) - Android View Extensions support for `DynamicListAdapter`.

# License

    Copyright 2020 Tyler Wong

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
