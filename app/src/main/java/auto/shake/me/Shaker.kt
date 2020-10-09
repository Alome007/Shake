package auto.shake.me

import android.content.Context
import android.hardware.SensorListener
import android.hardware.SensorManager
import java.lang.Math.sqrt

class Shaker(private val mContext: Context) : SensorListener {
    private var mSensorMgr: SensorManager? = null
    private var mLastX = -1.0f
    private var mLastY = -1.0f
    private var mLastZ = -1.0f
    private var mLastTime: Long = 0
    private var mShakeListener: OnShakeListener? = null
    private var mShakeCount = 0
    private var mLastShake: Long = 0
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private var mLastForce: Long = 0

    interface OnShakeListener {
        fun onShake()
    }

    fun setOnShakeListener(listener: OnShakeListener?) {
        mShakeListener = listener
    }

    fun resume() {
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
        mSensorMgr =
            mContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (mSensorMgr == null) {
            throw UnsupportedOperationException("Sensors not supported")
        }
        val supported = mSensorMgr!!.registerListener(
            this,
            SensorManager.SENSOR_ACCELEROMETER,
            SensorManager.SENSOR_DELAY_GAME
        )
        if (!supported) {
            mSensorMgr!!.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER)
            throw UnsupportedOperationException("Accelerometer not supported")
        }
    }

    fun pause() {
        if (mSensorMgr != null) {
            mSensorMgr!!.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER)
            mSensorMgr = null
        }
    }

    override fun onAccuracyChanged(sensor: Int, accuracy: Int) {}
    override fun onSensorChanged(sensor: Int, values: FloatArray) {
            mLastX = values[SensorManager.DATA_X]
            mLastY = values[SensorManager.DATA_Y]
            mLastZ = values[SensorManager.DATA_Z]


            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((mLastX * mLastY + mLastY * mLastY + mLastZ * mLastZ).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            if (acceleration > 12) {
                if (mShakeListener != null) {
                    mShakeListener!!.onShake()
                }
            }


    }

    companion object {
        private const val FORCE_THRESHOLD = 350
        private const val TIME_THRESHOLD = 200
        private const val SHAKE_TIMEOUT = 500
        private const val SHAKE_DURATION = 1000
        private const val SHAKE_COUNT = 3
    }

    init {
        resume()
    }
}