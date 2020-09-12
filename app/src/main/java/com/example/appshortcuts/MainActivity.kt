package com.example.appshortcuts

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onAddDynamicShortcut(view: View) {
        val context = applicationContext
        val shortcutManager = getSystemService<ShortcutManager>(ShortcutManager::class.java)
        val shortcut = ShortcutInfo.Builder(context, "shortcutId1")
                .setShortLabel("Website")
                .setLongLabel("Open the website")
                .setDisabledMessage("This shortcut is disabled")
                .setIcon(Icon.createWithResource(context, R.drawable.link))
                .setIntent(Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.mysite.example.com/")))
                .build()

        shortcutManager.setDynamicShortcuts(listOf(shortcut))
    }

    fun onGetShortcutInfo(view: View) {
        val context = applicationContext
        val shortcutManager = getSystemService<ShortcutManager>(ShortcutManager::class.java)

        // Static shortcuts
        val manifestShortcuts: List<ShortcutInfo> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            shortcutManager.getShortcuts(ShortcutManager.FLAG_MATCH_MANIFEST)
        } else {
            shortcutManager.getManifestShortcuts()
        }
        for (shortcut:ShortcutInfo in manifestShortcuts) {
            Log.d(TAG, "Manifest ShortcutInfo: $shortcut")
        }

        // Dynamic shortcuts
        val dynamicShortcuts = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            shortcutManager.getShortcuts(ShortcutManager.FLAG_MATCH_DYNAMIC)
        } else {
            shortcutManager.getDynamicShortcuts()
        }
        for (shortcut:ShortcutInfo in dynamicShortcuts) {
            Log.d(TAG, "Dynamic ShortcutInfo: $shortcut")
        }

        // Pinned shortcuts
        val pinnedShortcuts = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            shortcutManager.getShortcuts(ShortcutManager.FLAG_MATCH_PINNED)
        } else {
            shortcutManager.getPinnedShortcuts()
        }
        for (shortcut:ShortcutInfo in pinnedShortcuts) {
            Log.d(TAG, "Pinned ShortcutInfo: $shortcut")
        }
    }

    fun onUpdateShortcut(view: View) {
        val context = applicationContext
        val shortcutManager = getSystemService<ShortcutManager>(ShortcutManager::class.java)
        val shortcut = ShortcutInfo.Builder(context, "shortcutId1")
                .setShortLabel("Website")
                .setLongLabel("Open! the website")
                .setDisabledMessage("This shortcut is disabled")
                .setIcon(Icon.createWithResource(context, R.drawable.link))
                .setIntent(Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.mysite.example.com/")))
                .build()

        shortcutManager.updateShortcuts(listOf(shortcut))
    }

    fun onRemoveDynamicShortcut(view: View) {
        val context = applicationContext
        val shortcutManager = getSystemService<ShortcutManager>(ShortcutManager::class.java)
        shortcutManager.removeDynamicShortcuts(listOf("shortcutId1"))
        // shortcutManager.removeAllDynamicShortcuts()
    }

    fun onAddPinnedShortcut(view: View) {
        val context = applicationContext
        val shortcut = ShortcutInfo.Builder(context, "pinnedShortcutId1")
                .setShortLabel("Website")
                .setLongLabel("Open the website")
                .setDisabledMessage("This shortcut is disabled")
                .setIcon(Icon.createWithResource(context, R.drawable.link))
                .setIntent(Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.mysite.example.com/")))
                .build()

        val shortcutManager = getSystemService<ShortcutManager>(ShortcutManager::class.java)
        if (shortcutManager.isRequestPinShortcutSupported) {
            val pinnedShortcutCallbackIntent =
                    shortcutManager.createShortcutResultIntent(shortcut)

            val successCallback = PendingIntent.getBroadcast(context, /* request code */ 0,
                    pinnedShortcutCallbackIntent, /* flags */ 0)

            shortcutManager.requestPinShortcut(shortcut, successCallback.intentSender)
        }
    }

    fun onDisableShortcut(view: View) {
        val context = applicationContext
        val shortcutManager = getSystemService<ShortcutManager>(ShortcutManager::class.java)
        shortcutManager.disableShortcuts(listOf("pinnedShortcutId1"))
        // shortcutManager.enableShortcuts(listOf("pinnedShortcutId1"))
    }

}