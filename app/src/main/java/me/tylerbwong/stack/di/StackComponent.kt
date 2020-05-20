package me.tylerbwong.stack.di

import android.content.Context
import dagger.Component
import me.tylerbwong.stack.data.DeepLinker
import me.tylerbwong.stack.data.auth.di.AuthModule
import me.tylerbwong.stack.data.network.di.NetworkModule
import me.tylerbwong.stack.ui.DeepLinkingActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AuthModule::class, NetworkModule::class, StackModule::class])
interface StackComponent {
    fun context(): Context
    fun deepLinker(): DeepLinker

    fun inject(activity: DeepLinkingActivity)
}
