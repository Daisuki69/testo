package com.siren.homeproxy

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * SirenBootReceiver
 *
 * Fires on BOOT_COMPLETED (and OEM fast-boot variants).
 * Immediately launches the target app to ensure it's alive before the user
 * even reaches their home screen.
 *
 * Target app is configured in [SirenConfig].
 */
class SirenBootReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "SirenBoot"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val bootActions = setOf(
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_LOCKED_BOOT_COMPLETED,
            "android.intent.action.QUICKBOOT_POWERON",
            "com.htc.intent.action.QUICKBOOT_POWERON"
        )

        if (intent.action !in bootActions) return

        Log.i(TAG, "Boot detected [${intent.action}] — launching target: ${SirenConfig.TARGET_PACKAGE}")

        try {
            val targetIntent = Intent().apply {
                component = ComponentName(SirenConfig.TARGET_PACKAGE, SirenConfig.TARGET_ACTIVITY)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(targetIntent)
            Log.i(TAG, "Target launched on boot.")
        } catch (e: Exception) {
            Log.w(TAG, "Explicit boot launch failed, trying package launch intent: ${e.message}")
            try {
                context.packageManager.getLaunchIntentForPackage(SirenConfig.TARGET_PACKAGE)?.let { launchIntent ->
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(launchIntent)
                    Log.i(TAG, "Target launched via package intent on boot.")
                }
            } catch (e2: Exception) {
                Log.e(TAG, "Failed to launch target on boot: ${e2.message}")
            }
        }
    }
}
