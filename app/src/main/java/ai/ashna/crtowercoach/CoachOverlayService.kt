package ai.ashna.crtowercoach

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.media.projection.MediaProjectionManager
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView

class CoachOverlayService : Service() {
    companion object {
        const val RESULT_CODE = "resultCode"
        const val RESULT_DATA = "resultData"
        private const val CHANNEL = "coach"
    }

    private var overlay: TextView? = null

    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(CHANNEL, "Live coach", NotificationManager.IMPORTANCE_LOW)
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        startForeground(7, Notification.Builder(this, CHANNEL)
            .setContentTitle("CR Tower Coach running")
            .setContentText("Analyzing the game screen")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showOverlay()
        // Production model hook: pass MediaProjection frames to an on-device detector here.
        return START_STICKY
    }

    private fun showOverlay() {
        if (overlay != null) return
        overlay = TextView(this).apply {
            text = "COACH READY\nScreen detector starting…\nDefend: waiting for troop detection"
            textSize = 13f
            setTextColor(Color.WHITE)
            setBackgroundColor(0xCC171923.toInt())
            setPadding(22, 14, 22, 14)
        }
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL; y = 80 }
        getSystemService(Context.WINDOW_SERVICE).let { (it as WindowManager).addView(overlay, params) }
    }

    override fun onDestroy() {
        overlay?.let { (getSystemService(WINDOW_SERVICE) as WindowManager).removeView(it) }
        overlay = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
