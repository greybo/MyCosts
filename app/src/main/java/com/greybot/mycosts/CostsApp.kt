package com.greybot.mycosts

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.greybot.mycosts.analytics.AnalyticsManager


class CostsApp : Application() {
    var mFirebaseAnalytics: FirebaseAnalytics? = null

    companion object {
        lateinit var share: CostsApp
    }

    override fun onCreate() {
        super.onCreate()
        // Obtain the FirebaseAnalytics instance.
        share = this
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        AnalyticsManager().sendAnalytics(BuildConfig.APPLICATION_ID,"test")
    }
}