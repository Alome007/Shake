package auto.shake.me

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import safety.com.br.android_shake_detector.core.ShakeDetector
import safety.com.br.android_shake_detector.core.ShakeOptions


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, ShakerService::class.java)
        startService(intent)
        val options = ShakeOptions()
            .background(true)
            .interval(1000)
            .shakeCount(2)
            .sensibility(2.0f)
        val shakeDetector = ShakeDetector(options).start(
            this
        ) {
            val vibrator = this?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }
        }
    }
}
