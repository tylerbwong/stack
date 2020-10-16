package me.tylerbwong.stack.api.service

import me.tylerbwong.stack.api.model.ACTIVITY
import me.tylerbwong.stack.api.model.DESC
import me.tylerbwong.stack.api.model.Order
import me.tylerbwong.stack.api.model.Sort

// query params
internal const val ACCESS_TOKEN = "accessToken"
const val SITE_PARAM = "site"
internal const val PAGE_SIZE_PARAM = "pagesize"
internal const val PAGE_PARAM = "page"
internal const val FILTER_PARAM = "filter"
internal const val TAGGED_PARAM = "tagged"
internal const val KEY_PARAM = "key"

// defaults
const val DEFAULT_SITE = "stackoverflow"
@Sort
internal const val DEFAULT_SORT = ACTIVITY
@Order
internal const val DEFAULT_ORDER = DESC
internal const val DEFAULT_PAGE_SIZE = 75
internal const val MAX_PAGE_SIZE = 100
internal const val DEFAULT_PAGE = 1
