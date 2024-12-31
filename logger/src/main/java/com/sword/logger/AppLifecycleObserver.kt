package com.sword.logger

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

class AppLifecycleObserver(private val onAppCreate: (() -> Unit)? = null, private val onAppDestory: (() -> Unit)? = null) : ActivityLifecycleCallbacks {
    private var activityCount = 0

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityCount++
        if (activityCount == 1) {
            onAppCreate?.invoke()
        }
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        activityCount--
        if (activityCount == 0) {
            onAppDestory?.invoke()
        }
    }

}
