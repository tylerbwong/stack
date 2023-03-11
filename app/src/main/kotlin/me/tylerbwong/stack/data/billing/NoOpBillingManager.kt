package me.tylerbwong.stack.data.billing

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NoOpBillingManager : BillingManager {

    override val availableProducts: LiveData<List<Product>> = MutableLiveData()
    override val purchaseSuccess: LiveData<Boolean?> = MutableLiveData()

    override fun startConnection() {
        // No-op
    }

    override fun queryProductDetailsAsync() {
        // No-op
    }

    override fun launchBillingFlow(activity: Activity, product: Product) {
        // No-op
    }

    override fun endConnection() {
        // No-op
    }
}
