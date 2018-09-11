package com.opengles_demo.fragments

import android.opengl.GLES20.*
import android.opengl.Matrix
import asiainnovations.com.opengles_demo.GlShader
import asiainnovations.com.opengles_demo.fragments.BaseGLFragment
import asiainnovations.com.opengles_demo.getAssetAsString
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

abstract class MVPFragment: BaseGLFragment() {

    //透视矩阵
    protected val mProjMatrix = FloatArray(16)
    //视图矩阵
    protected val mVMatrix = FloatArray(16)
    //透视矩阵与视图矩阵变换后的总矩阵
    protected val mMVPMatrix = FloatArray(16)

    protected lateinit var shader: GlShader
    private var lastDrawTime: Long = 0
    protected var deltaTime = 0L

    final override fun onDrawFrame(gl: GL10) {
        val time = System.currentTimeMillis()
        deltaTime = time - lastDrawTime
        lastDrawTime = time
        onEarlyDrawFrame()
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0)
        glClearColor(0f, 0f, 0f, 0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        onDrawFrame()
    }

    open fun onEarlyDrawFrame(){}

    abstract fun onDrawFrame()

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        glClearDepthf(1.0f)
        glEnable(GL_DEPTH_TEST)
        glDepthFunc(GL_LEQUAL)
        glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 7f);

        mVMatrix.apply {
            Matrix.setIdentityM(this, 0)
            Matrix.translateM(this, 0, 0f, 0f, -2f)
            Matrix.scaleM(this, 0, 0.5f, 0.5f, 0.5f)
        }
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        lastDrawTime = System.currentTimeMillis()
        shader = GlShader(vertexShader(),fragmentShader())
        shader.useProgram()
    }

    open fun vertexShader() = getAssetAsString(resources, "simple/vertex_shader.glsl")!!
    open fun fragmentShader() = getAssetAsString(resources, "simple/fragment_shader.glsl")!!
}