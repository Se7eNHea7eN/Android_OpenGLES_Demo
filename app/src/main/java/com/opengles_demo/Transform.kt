package com.opengles_demo

import android.opengl.Matrix
import com.opengles_demo.math.Vector3

class Transform {
    var position = Vector3(0f, 0f, 0f)
    var rotation = Quaternion.fromEular(Vector3.Zero())
    var scale = Vector3(1f, 1f, 1f)

    fun up(): Vector3 {
        return Vector3.Up().rotate(rotation)
    }

    fun forward(): Vector3 {
        return Vector3.Forward().rotate(rotation)
    }

    fun matrix(): FloatArray {
        val result = rotation.toMatrix()
        Matrix.translateM(result, 0, position.x, position.y, position.z)
        Matrix.scaleM(result, 0, scale.x, scale.y, scale.z)
        return result
    }

    fun worldPosition(): Vector3 {
        return position.rotate(rotation) * scale
    }
}