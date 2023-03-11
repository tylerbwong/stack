package me.tylerbwong.stack.play.billing

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import me.tylerbwong.stack.data.billing.BillingManager
import me.tylerbwong.stack.data.billing.Product
import me.tylerbwong.stack.data.billing.Sku
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import timber.log.Timber

class PlayBillingManager(context: Context) : BillingManager, PurchasesUpdatedListener {

    override val availableProducts: LiveData<List<Product>>
        get() = _availableProducts
    private val _availableProducts = MutableLiveData<List<Product>>()

    override val purchaseSuccess: LiveData<Boolean?>
        get() = _purchaseSuccess
    private val _purchaseSuccess = SingleLiveEvent<Boolean?>()

    private var remoteProductDetails: List<ProductDetails>? = null

    private val billingClient = BillingClient.newBuilder(context.applicationContext)
        .enablePendingPurchases()
        .setListener(this)
        .build()

    override fun startConnection() {
        if (!billingClient.isReady) {
            billingClient.startConnection(
                object : BillingClientStateListener {
                    override fun onBillingServiceDisconnected() {
                        Timber.e("Play billing client disconnected")
                    }

                    override fun onBillingSetupFinished(result: BillingResult) {
                        when (result.responseCode) {
                            BillingClient.BillingResponseCode.OK -> {
                                Timber.i("Play billing client connected. Fetching products.")
                                queryProductDetailsAsync()
                            }
                            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> {
                                Timber.e(result.debugMessage)
                            }
                        }
                    }
                }
            )
        }
    }

    override fun queryProductDetailsAsync() {
        val productList = inAppItems.map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it.productId)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()
        billingClient.queryProductDetailsAsync(params) { result, productDetails ->
            when (result.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    Timber.i("Successfully queried product details")
                    remoteProductDetails = productDetails.toList()
                    _availableProducts.postValue(
                        productDetails
                            .map {
                                Product(
                                    id = it.productId,
                                    name = it.name,
                                    description = it.description,
                                    rawPrice = it.oneTimePurchaseOfferDetails?.priceAmountMicros,
                                    formattedPrice = it.oneTimePurchaseOfferDetails?.formattedPrice,
                                )
                            }
                            .sortedBy { it.rawPrice }
                    )
                }
                else -> {
                    Timber.e(result.debugMessage)
                    _availableProducts.postValue(emptyList())
                }
            }
        }
    }

    override fun launchBillingFlow(activity: Activity, product: Product) {
        val availableProductDetails = remoteProductDetails?.find { it.productId == product.id }
            ?: run {
                queryProductDetailsAsync()
                return
            }
        val params = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(availableProductDetails)
                        .build()
                )
            )
            .build()
        billingClient.launchBillingFlow(activity, params)
    }

    override fun endConnection() = billingClient.endConnection()

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        when (result.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (purchases != null) {
                    consumePurchase(purchases)
                    _purchaseSuccess.postValue(true)
                }
            }
            else -> _purchaseSuccess.postValue(false)
        }
    }

    private fun consumePurchase(purchases: List<Purchase>) {
        purchases.forEach {
            val params = ConsumeParams.newBuilder()
                .setPurchaseToken(it.purchaseToken)
                .build()
            billingClient.consumeAsync(params) { result, _ ->
                when (result.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {
                        Timber.i("Purchase successfully consumed")
                    }
                    else -> {
                        Timber.e("Failed to consume purchase")
                    }
                }
            }
        }
    }

    companion object {
        val inAppItems = listOf(
            Sku(productId = "stack_donation_tier_1"),
            Sku(productId = "stack_donation_tier_2"),
            Sku(productId = "stack_donation_tier_3"),
            Sku(productId = "stack_donation_tier_4"),
        )
    }
}
