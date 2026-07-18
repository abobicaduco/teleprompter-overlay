package com.abobicaduco.teleprompter_overlay

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    companion object {
        private const val CHANNEL = "com.abobicaduco.teleprompter_overlay/app"
        private const val PREFS = "FlutterSharedPreferences"
        private const val KEY_STAY_BACK = "flutter.stay_in_background"
        private const val OVERLAY_CHANNEL_ID = "Overlay Channel"
    }

    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ensureQuietOverlayChannel()
        if (isLauncherIntent(intent)) {
            // Usuário abriu pelo ícone de propósito → pode editar o roteiro.
            clearStayInBackground()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (isLauncherIntent(intent)) {
            clearStayInBackground()
        }
    }

    override fun onResume() {
        super.onResume()
        // Se o overlay está ativo e alguém trouxe a Activity sem ser o launcher,
        // some de novo (ex.: toque na notificação).
        if (!isLauncherIntent(intent)) {
            maybeHideWhenOverlayActive()
        }
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
            .setMethodCallHandler { call, result ->
                when (call.method) {
                    "closeAppUi" -> {
                        // Fecha a tela do app; o OverlayService (FGS) continua.
                        moveTaskToBack(true)
                        mainHandler.postDelayed({
                            if (!isFinishing) {
                                finishAndRemoveTask()
                            }
                        }, 120)
                        result.success(true)
                    }
                    "ensureQuietNotificationChannel" -> {
                        ensureQuietOverlayChannel()
                        result.success(true)
                    }
                    else -> result.notImplemented()
                }
            }
    }

    private fun maybeHideWhenOverlayActive() {
        if (!shouldStayInBackground()) return
        moveTaskToBack(true)
        mainHandler.postDelayed({
            if (!isFinishing && shouldStayInBackground()) {
                finishAndRemoveTask()
            }
        }, 80)
    }

    private fun shouldStayInBackground(): Boolean {
        return getSharedPreferences(PREFS, MODE_PRIVATE)
            .getBoolean(KEY_STAY_BACK, false)
    }

    private fun clearStayInBackground() {
        getSharedPreferences(PREFS, MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_STAY_BACK, false)
            .apply()
    }

    private fun isLauncherIntent(intent: Intent?): Boolean {
        if (intent == null) return false
        return Intent.ACTION_MAIN == intent.action &&
            intent.hasCategory(Intent.CATEGORY_LAUNCHER)
    }

    private fun ensureQuietOverlayChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = getSystemService(NotificationManager::class.java) ?: return
        val existing = manager.getNotificationChannel(OVERLAY_CHANNEL_ID)
        if (existing != null && existing.importance > NotificationManager.IMPORTANCE_LOW) {
            manager.deleteNotificationChannel(OVERLAY_CHANNEL_ID)
        }
        if (manager.getNotificationChannel(OVERLAY_CHANNEL_ID) == null) {
            val channel = NotificationChannel(
                OVERLAY_CHANNEL_ID,
                "PromptCue teleprompter",
                NotificationManager.IMPORTANCE_LOW,
            ).apply {
                description = "Mantém o teleprompter ativo sobre outros apps"
                setShowBadge(false)
                enableLights(false)
                enableVibration(false)
                setSound(null, null)
            }
            manager.createNotificationChannel(channel)
        }
    }
}
