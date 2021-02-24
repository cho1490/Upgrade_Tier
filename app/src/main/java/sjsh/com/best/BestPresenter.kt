package sjsh.com.best

import android.view.View
import com.google.android.gms.ads.*
import java.util.*

class BestPresenter() {

    fun loadBanner(view: View): AdView {
        var adView: AdView? = null

        val testDevices: MutableList<String> = ArrayList()
        testDevices.add(AdRequest.DEVICE_ID_EMULATOR)
        val requestConfiguration = RequestConfiguration.Builder()
                .setTestDeviceIds(testDevices)
                .build()
        MobileAds.setRequestConfiguration(requestConfiguration)

        adView = AdView(view.context)
        adView.adUnitId = "ca-app-pub-6713667729006874/8545645874"
        adView.adSize = AdSize.SMART_BANNER
        adView.loadAd(AdRequest.Builder().build())

        return adView
    }

    fun getData(data: String) {

    }
}