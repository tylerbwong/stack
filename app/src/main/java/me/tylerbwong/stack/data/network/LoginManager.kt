package me.tylerbwong.stack.data.network

import android.content.Context
import android.net.Uri
import me.tylerbwong.stack.data.model.NO_EXPIRY
import me.tylerbwong.stack.data.model.Scope
import me.tylerbwong.stack.presentation.utils.launchCustomTab

object LoginManager {
    const val CLIENT_ID = "12074"
    const val LOGIN_URL = "https://stackoverflow.com/oauth/dialog"

    fun startLogin(context: Context, url: String? = null) {
        val launchUrl = url ?: LoginUrlBuilder()
                .setClientId()
                .setScope()
                .setRedirect()
                .setState()
                .build()
        launchCustomTab(context, launchUrl)
    }

    class LoginUrlBuilder {
        private lateinit var clientId: String
        private lateinit var scope: String
        private lateinit var redirectUri: String
        private lateinit var state: String

        fun build() = "$LOGIN_URL$clientId$scope$redirectUri$state"

        fun setClientId(clientId: String = CLIENT_ID): LoginUrlBuilder {
            this.clientId = "?client_id=$clientId"
            return this
        }

        fun setScope(@Scope scope: String = NO_EXPIRY): LoginUrlBuilder {
            this.scope = "&scope=$scope"
            return this
        }

        fun setRedirect(redirectUri: Uri? = null): LoginUrlBuilder {
            this.redirectUri = "&redirect_uri=$redirectUri"
            return this
        }

        fun setState(state: String = ""): LoginUrlBuilder {
            this.state = "&state=$state"
            return this
        }
    }
}