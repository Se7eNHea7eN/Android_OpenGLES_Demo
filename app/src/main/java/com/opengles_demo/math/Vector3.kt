package com.opengles_demo.math

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

data class Vector3(var x: Float, var y: Float, var z: Float) {

    companion object {
        val Up = Vector3(0f, 1f, 0f)
        val Forward = Vector3(0f, 0f, 1f)
        val Right = Vector3(1f, 0f, 0f)

        fun dot(a: Vector3, b: Vector3): Float = a.x * b.x + a.y * b.y + a.z * b.z
        fun cross(a: Vector3, b: Vector3): Vector3 =
                Vector3(a.y * b.z - b.y * a.z, a.z * b.x - b.z * a.x, a.x * b.y - b.x * a.y)

        fun rotate(v: Vector3, axis: Vector3, theta: Float): Vector3 {
            val cos_theta = Math.cos(theta.toDouble()).toFloat()
            val sin_theta = Math.sin(theta.toDouble()).toFloat()

            return (v * cos_theta) + (cross(axis, v) * sin_theta) + (axis * dot(axis, v)) * (1 - cos_theta)
        }
    }

    fun magnitude(): Float {
        return Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
    }

    fun dot(vector3: Vector3) = dot(this, vector3)

    fun cross(vector3: Vector3) = cross(this, vector3)

    fun rotate(axis: Vector3, theta: Float) = rotate(this, axis, theta)

    fun asFloatArray(): FloatArray = floatArrayOf(x, y, z)
    fun asFloatBuffer(): FloatBuffer = ByteBuffer.allocateDirect(12)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(asFloatArray())
                position(0)
            }

    operator fun plus(other: Vector3): Vector3 = Vector3(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Vector3): Vector3 = Vector3(x - other.x, y - other.y, z - other.z)

    operator fun times(factor: Float): Vector3 = Vector3(x * factor, y * factor, z * factor)

    operator fun Float.times(vector3: Vector3): Vector3 = vector3 * this
}