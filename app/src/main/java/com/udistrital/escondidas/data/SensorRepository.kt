package com.udistrital.escondidas.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SensorRepository(context: Context) {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magnetometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    fun getOrientationFlow(): Flow<Float> = callbackFlow {
        if (accelerometer == null || magnetometer == null) {
            close(IllegalStateException("El dispositivo no cuenta con acelerómetro o magnetómetro."))
            return@callbackFlow
        }

        val gravity = FloatArray(3)
        val geomagnetic = FloatArray(3)
        val rotationMatrix = FloatArray(9)
        val orientationAngles = FloatArray(3)

        var hasGravity = false
        var hasGeomagnetic = false

        val ALPHA = 0.15f // Coeficiente del filtro pasa-bajos

        fun lowPass(input: FloatArray, output: FloatArray) {
            for (i in input.indices) {
                output[i] = output[i] + ALPHA * (input[i] - output[i])
            }
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event ?: return
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        lowPass(event.values, gravity)
                        hasGravity = true
                    }
                    Sensor.TYPE_MAGNETIC_FIELD -> {
                        lowPass(event.values, geomagnetic)
                        hasGeomagnetic = true
                    }
                }

                if (hasGravity && hasGeomagnetic) {
                    val success = SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)
                    if (success) {
                        SensorManager.getOrientation(rotationMatrix, orientationAngles)
                        val azimuthRad = orientationAngles[0]
                        val azimuthDeg = ((Math.toDegrees(azimuthRad.toDouble()).toFloat() + 360.0f) % 360.0f)
                        trySend(azimuthDeg)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(listener, magnetometer, SensorManager.SENSOR_DELAY_GAME)

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }
}