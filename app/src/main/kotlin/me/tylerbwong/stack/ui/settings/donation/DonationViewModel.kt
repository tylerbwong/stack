package me.tylerbwong.stack.ui.settings.donation

import android.app.Activity
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.tylerbwong.stack.data.billing.BillingManager
import me.tylerbwong.stack.data.billing.NoOpBillingManager
import me.tylerbwong.stack.data.billing.Product
import javax.inject.Inject

@HiltViewModel
class DonationViewModel @Inject constructor(
    private val billingManager: BillingManager,
) : ViewModel() {

    internal val isBillingAvailable: Boolean
        get() = billingManager !is NoOpBillingManager

    internal val availableProducts = billingManager.availableProducts
    internal val purchaseSuccess = billingManager.purchaseSuccess

    internal fun startConnection() = billingManager.startConnection()

    internal fun launchBillingFlow(
        activity: Activity,
        product: Product,
    ) = billingManager.launchBillingFlow(activity, product)

    internal fun markConfirmationSeen() = billingManager.markConfirmationSeen()
}
