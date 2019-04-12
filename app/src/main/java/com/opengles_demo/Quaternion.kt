package com.opengles_demo

import com.opengles_demo.math.Vector3
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


class Quaternion(var w: Float, var x: Float, var y: Float, var z: Float) {
    companion object {
        fun fromEular(euler: Vector3): Quaternion {
            val cy = cos(euler.y * 0.5f)
            val sy = sin(euler.y * 0.5f)
            val cp = cos(euler.x * 0.5f)
            val sp = sin(euler.x * 0.5f)
            val cr = cos(euler.z * 0.5f)
            val sr = sin(euler.z * 0.5f)
            return Quaternion(
                    cy * cp * cr + sy * sp * sr,
                    cy * cp * sr - sy * sp * cr,
                    sy * cp * sr + cy * sp * cr,
                    sy * cp * cr - cy * sp * sr
            )
        }
    }

    fun toEuler(): Vector3 {
        val sinr_cosp = +2.0f * (w * x + y * z)
        val cosr_cosp = +1.0f - 2.0f * (x * x + y * y)
        val roll = atan2(sinr_cosp, cosr_cosp)

        // pitch (y-axis rotation)
        val sinp = +2.0f * (w * y - z * x)
        val pitch = asin(sinp)

        // yaw (z-axis rotation)
        val siny_cosp = +2.0f * (w * z + x * y)
        val cosy_cosp = +1.0f - 2.0f * (y * y + z * z)
        val yaw = atan2(siny_cosp, cosy_cosp)

        return Vector3(
                pitch,
                yaw,
                roll
        )
    }

    fun rotate(rotation:Quaternion){
        val result = this*rotation
        w = result.w
        x = result.x
        y = result.y
        z = result.z
    }
    fun toMatrix(): FloatArray {
        return floatArrayOf(
                1 - 2 * y * y - 2 * z * z, 2 * (x * y - z * w), 2 * (x * z + y * w), 0f,
                2 * (x * y + z * w), 1 - 2 * x * x - 2 * z * z, 2 * (y * z - x * w), 0f,
                2 * (x * z - y * w), 2 * (y * z + x * w), 1 - 2 * x * x - 2 * y * y, 0f,
                0f, 0f, 0f, 1f
        )
    }

    operator fun times(other: Quaternion): Quaternion = Quaternion(
            w * other.w - x * other.x - y * other.y - z * other.z,
            x * other.w + w * other.x + y * other.z - z * other.y,
            y * other.w + w * other.y + z * other.x - x * other.z,
            z * other.w + w * other.z + x * other.y - y * other.x
    )

    operator fun plus(other: Quaternion): Quaternion = Quaternion(
            w + other.w,
            x + other.x,
            y + other.y,
            z + other.z
    )
}