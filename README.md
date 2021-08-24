# Stack
[![Build](https://github.com/tylerbwong/stack/actions/workflows/build.yml/badge.svg)](https://github.com/tylerbwong/stack/actions/workflows/build.yml)

![Stack](/.idea/icon.png)

Stack is an Android application that lets you browse [stackoverflow.com](https://stackoverflow.com) and other Stack Exchange sites. It is powered by the [Stack Exchange API](https://api.stackexchange.com/).

### Download

<a href="https://play.google.com/store/apps/details?id=me.tylerbwong.stack"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" height=80px /></a>

### Development

The goal of this project is to provide examples for the latest Android libraries and tools. A few noteworthy examples include:

* [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) for asynchronous code
* [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection
* [Jetpack Compose](https://developer.android.com/jetpack/compose) for declarative UI code written in Kotlin (Slowly being introduced)
* [Room](https://developer.android.com/jetpack/androidx/releases/room) for local data persistence
* [Markwon](https://github.com/noties/Markwon) for rendering markdown

### Features

* View questions, answers, and comments with rich Markdown previews
* Switch to any Stack Exchange site
* Log in to vote for questions or post new questions and answers (Saving drafts supported)
* Bookmark questions to save for later (Supports offline)
* Search for questions with advanced filter controls
* View other users' profiles
* Dark mode
* Question deep linking

### Subprojects

Here you can also find a few useful Gradle modules as well:

* [buildSrc](./buildSrc) - A Gradle module for common build logic with a custom plugin example.
* [compose-markdown](./compose-markdown) - A Jetpack Compose Text component that can render markdown content using [intellij-markdown](https://github.com/valich/intellij-markdown) for parsing.
* [compose-preference](./compose-preference) - A set of Jetpack Compose Preference components.
* [dynamic-list-adapter](./dynamic-list-adapter) - An abstraction around `RecyclerView` built on `ListAdapter` for asynchronous list diffing.
* [dynamic-list-adapter-viewbinding](./dynamic-list-adapter-viewbinding) - View Binding support for `DynamicListAdapter`.

# License

    Copyright (C) 2020 Tyler Wong

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses.
