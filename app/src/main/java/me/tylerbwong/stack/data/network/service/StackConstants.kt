package me.tylerbwong.stack.data.network.service

import me.tylerbwong.stack.data.model.ACTIVITY
import me.tylerbwong.stack.data.model.DESC
import me.tylerbwong.stack.data.model.Order
import me.tylerbwong.stack.data.model.Sort

// query params
internal const val ACCESS_TOKEN = "accessToken"
internal const val SITE_PARAM = "site"
internal const val PAGE_SIZE_PARAM = "pagesize"
internal const val PAGE_PARAM = "page"
internal const val FILTER_PARAM = "filter"
internal const val TAGGED_PARAM = "tagged"
internal const val KEY_PARAM = "key"
internal const val SEARCH_PARAM = "q"

// defaults
internal const val DEFAULT_SITE = "stackoverflow"
@Sort
internal const val DEFAULT_SORT = ACTIVITY
@Order
internal const val DEFAULT_ORDER = DESC
internal const val DEFAULT_PAGE_SIZE = 75
internal const val DEFAULT_PAGE = 1
