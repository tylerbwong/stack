package me.tylerbwong.stack.data.model

import android.support.annotation.StringDef

const val ORDER_PARAM = "order"
const val DESC = "desc"
const val ASC = "asc"

@StringDef(DESC, ASC)
annotation class Order
