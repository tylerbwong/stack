package me.tylerbwong.stack.data.model

import android.support.annotation.StringDef

const val READ_INBOX = "read_inbox"
const val NO_EXPIRY = "no_expiry"
const val WRITE_ACCESS = "write_access"
const val PRIVATE_INFO = "private_info"

@StringDef(READ_INBOX, NO_EXPIRY, WRITE_ACCESS, PRIVATE_INFO)
annotation class Scope
