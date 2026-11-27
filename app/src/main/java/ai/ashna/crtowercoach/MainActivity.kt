package ai.ashna.crtowercoach

import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private val captureRequest = 4201

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val status = TextView(this).apply {
            text = "CR TOWER COACH\n\nThis coach reads your screen and displays defensive advice.\nIt never taps or controls the game.\n\nGrant overlay permission, then start capture."
            textSize = 17f
            setPadding(32, 48, 32, 24)
        }
        val overlay = Button(this).apply {
            text = "Grant overlay permission"
            setOnClickListener { startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))) }
        }
        val start = Button(this).apply {
            text = "Start live coach"
            setOnClickListener { requestCapture() }
        }
        setContentView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(status)
            addView(overlay)
            addView(start)
        })
    }

    private fun requestCapture() {
        val manager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(manager.createScreenCaptureIntent(), captureRequest)
    }

    @Deprecated("Activity result API kept minimal for the prototype")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == captureRequest && resultCode == RESULT_OK && data != null) {
            startForegroundService(Intent(this, CoachOverlayService::class.java).apply {
                putExtra(CoachOverlayService.RESULT_CODE, resultCode)
                putExtra(CoachOverlayService.RESULT_DATA, data)
            })
        }
    }
}
