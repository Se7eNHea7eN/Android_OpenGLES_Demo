package com.opengles_demo

import android.opengl.GLES20
import android.opengl.Matrix

class GlCamera : GlObject() {
    //透视矩阵
    protected val projectionMatrix = FloatArray(16)

    //视矩阵
    protected val viewMatrix = FloatArray(16)

    fun onWindowSizeChanged(width: Int, height: Int){
        GLES20.glClearDepthf(1.0f)
//        glEnable(GL_CULL_FACE)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 100f)

    }

    fun render(objects:List<GlRenderObject>){
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        val forward = transform.forward()
        val up = transform.up()
        Matrix.setLookAtM(viewMatrix, 0, transform.position.x, transform.position.y, transform.position.z,
                forward.x,forward.y,forward.z,
                up.x, up.y, up.z)
        objects.forEach {
            it.setProjectionMatrix(projectionMatrix)
            it.setViewMatrix(viewMatrix)
            it.render()
        }
    }
}