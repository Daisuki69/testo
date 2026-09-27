package com.siren.homeproxy

import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log

/**
 * SirenHomeActivity
 *
 * Invisible HOME handler that implements the SIREN liveness proxy:
 *
 *   1. Android forwards HOME intent here (SIREN is the registered default Home).
 *   2. Check if the target app is running.
 *   3. If NOT running → launch the target app to revive it.
 *   4. Delegate HOME to the real launcher (discovered or cached).
 *
 * Target app is configured in [SirenConfig].
 * No UI. Translucent theme. Finishes immediately after delegation.
 */
class SirenHomeActivity : Activity() {

    companion object {
        private const val TAG = "SirenHome"

        private const val PREFS_NAME = "siren_home_proxy"
        private const val KEY_LAUNCHER_PKG = "selected_launcher_pkg"
        private const val KEY_LAUNCHER_CLS = "selected_launcher_cls"

        /** Delay before delegating to real launcher (gives target app time to start). */
        private const val REVIVE_DELAY_MS = 400L
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleHomeIntent()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleHomeIntent()
    }

    /**
     * Core flowchart logic:
     *   Is target running? → No  → Launch target → delegate after delay
     *   Is target running? → Yes → delegate immediately
     */
    private fun handleHomeIntent() {
        val targetAlive = isTargetRunning()
        Log.d(TAG, "Target [${SirenConfig.TARGET_PACKAGE}] alive: $targetAlive")

        if (!targetAlive) {
            launchTargetApp()
            handler.postDelayed(::delegateToRealLauncher, REVIVE_DELAY_MS)
        } else {
            delegateToRealLauncher()
        }
    }

    /**
     * Checks if the target app's process is currently alive via ActivityManager.
     */
    private fun isTargetRunning(): Boolean {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager ?: return false

        am.runningAppProcesses?.forEach { proc ->
            if (proc.processName == SirenConfig.TARGET_PACKAGE) {
                return proc.importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE
            }
        }

        @Suppress("DEPRECATION")
        am.getRunningServices(100)?.forEach { svc ->
            if (svc.service?.packageName == SirenConfig.TARGET_PACKAGE) {
                return true
            }
        }

        return false
    }

    /**
     * Launches the target app's configured Activity.
     */
    private fun launchTargetApp() {
        try {
            val targetIntent = Intent().apply {
                component = ComponentName(SirenConfig.TARGET_PACKAGE, SirenConfig.TARGET_ACTIVITY)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(targetIntent)
            Log.i(TAG, "Revived target: ${SirenConfig.TARGET_PACKAGE}")
        } catch (e: Exception) {
            Log.w(TAG, "Explicit launch failed, trying launch intent: ${e.message}")
            try {
                packageManager.getLaunchIntentForPackage(SirenConfig.TARGET_PACKAGE)?.let { launchIntent ->
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(launchIntent)
                    Log.i(TAG, "Revived target via launch intent: ${SirenConfig.TARGET_PACKAGE}")
                } ?: Log.e(TAG, "Target app not installed: ${SirenConfig.TARGET_PACKAGE}")
            } catch (e2: Exception) {
                Log.e(TAG, "Failed to launch target app: ${e2.message}")
            }
        }
    }

    /**
     * Delegates the HOME intent to the user's real launcher.
     * Priority: cached launcher → auto-discovered launcher.
     */
    private fun delegateToRealLauncher() {
        val realLauncher = getCachedLauncher() ?: discoverRealLauncher()

        if (realLauncher != null) {
            try {
                val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_HOME)
                    component = realLauncher
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_NO_ANIMATION or
                            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                }
                startActivity(homeIntent)
                Log.d(TAG, "Delegated HOME to: ${realLauncher.packageName}")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to delegate to launcher: ${e.message}")
            }
        } else {
            Log.e(TAG, "No real launcher found — cannot delegate HOME")
        }

        finish()
        @Suppress("DEPRECATION")
        overridePendingTransition(0, 0)
    }

    private fun getCachedLauncher(): ComponentName? {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val pkg = prefs.getString(KEY_LAUNCHER_PKG, null) ?: return null
        val cls = prefs.getString(KEY_LAUNCHER_CLS, null) ?: return null

        return try {
            packageManager.getActivityInfo(ComponentName(pkg, cls), 0)
            ComponentName(pkg, cls)
        } catch (_: Exception) {
            prefs.edit().remove(KEY_LAUNCHER_PKG).remove(KEY_LAUNCHER_CLS).apply()
            null
        }
    }

    private fun discoverRealLauncher(): ComponentName? {
        val myPkg = packageName
        val queryIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
        val candidates = packageManager.queryIntentActivities(queryIntent, PackageManager.MATCH_DEFAULT_ONLY)

        for (info in candidates) {
            val ai = info.activityInfo ?: continue
            val pkg = ai.packageName ?: continue
            val actName = ai.name ?: ""

            if (pkg == myPkg) continue
            if (pkg == SirenConfig.TARGET_PACKAGE) continue
            if (info.priority < 0) continue
            if (actName.lowercase().contains("fallbackhome")) continue
            if (pkg == "com.android.settings" || pkg.contains(".settings")) continue
            if (pkg.contains("setupwizard") || pkg.contains("provision")) continue

            getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
                .putString(KEY_LAUNCHER_PKG, pkg)
                .putString(KEY_LAUNCHER_CLS, ai.name)
                .apply()

            Log.i(TAG, "Discovered real launcher: $pkg / ${ai.name}")
            return ComponentName(pkg, ai.name)
        }

        return null
    }
}
