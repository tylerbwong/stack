package me.tylerbwong.stack.ui.utils

import android.view.View
import me.tylerbwong.stack.data.model.User

const val SHARED_COMPONENT_NAME = "transitionComp"

interface OnClickHandler {

    fun onClickOpenProfilePage(owner: User, view: View)

}