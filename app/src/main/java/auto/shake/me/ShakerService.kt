package auto.shake.me

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.IBinder
import android.widget.Toast
import auto.shake.me.Shaker.OnShakeListener

class ShakerService : Service(), OnShakeListener {
    private var mShaker: Shaker? = null
    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        mSensorManager =
            getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(1)
        mShaker = Shaker(this)
        mShaker!!.setOnShakeListener(this)
        super.onCreate()
    }

    override fun onShake() {
        Toast.makeText(this, "Shaking", Toast.LENGTH_SHORT).show()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        mSensorManager =
            getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(1)
        mShaker = Shaker(this)
        mShaker!!.setOnShakeListener(this)
        //your code here
        return START_STICKY
    }
}