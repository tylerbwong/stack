package me.tylerbwong.stack.data.billing

import android.app.Activity
import androidx.lifecycle.LiveData

interface BillingManager {

    val availableProducts: LiveData<List<Product>>

    val purchaseSuccess: LiveData<Boolean?>

    fun startConnection()

    fun queryProductDetailsAsync()

    fun launchBillingFlow(activity: Activity, product: Product)

    fun markConfirmationSeen()

    fun endConnection()
}
