package com.opengles_demo.fragments

import android.opengl.GLES20.*
import android.opengl.Matrix
import asiainnovations.com.opengles_demo.GlShader
import asiainnovations.com.opengles_demo.fragments.BaseGLFragment
import asiainnovations.com.opengles_demo.getAssetAsString
import com.opengles_demo.math.Vector3
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

abstract class MVPFragment : BaseGLFragment() {

    //透视矩阵
    protected val projectionMatrix = FloatArray(16)
    //模型矩阵
    protected val modelMatrix = FloatArray(16)
    //视矩阵
    protected val viewMatrix = FloatArray(16)

    protected lateinit var shader: GlShader
    private var lastDrawTime: Long = 0
    protected var deltaTime = 0L

    protected var eye = Vector3(0f, 0f, -5f)

    protected var look = Vector3.Forward

    protected var up = Vector3.Up

    final override fun onDrawFrame(gl: GL10) {
        val time = System.currentTimeMillis()
        deltaTime = time - lastDrawTime
        lastDrawTime = time
        onEarlyDrawFrame()
        shader.useProgram()
        shader.setUniformMatrix4fv("projectionMatrix", projectionMatrix)
        shader.setUniformMatrix4fv("modelMatrix", modelMatrix)
        shader.setUniformMatrix4fv("viewMatrix", viewMatrix)

        glClearColor(0f, 0f, 0f, 0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        onDrawFrame()
    }

    open fun onEarlyDrawFrame() {}

    abstract fun onDrawFrame()

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        glClearDepthf(1.0f)
//        glEnable(GL_CULL_FACE)
        glEnable(GL_DEPTH_TEST)
        glDepthFunc(GL_LEQUAL)
        glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 20f);

        Matrix.setIdentityM(modelMatrix, 0)


        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(viewMatrix, 0, eye.x, eye.y, eye.z, look.x, look.y, look.z, up.x, up.y, up.z)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        lastDrawTime = System.currentTimeMillis()
        shader = GlShader(vertexShader(), fragmentShader())
        shader.useProgram()
    }

    open fun vertexShader() = getAssetAsString(resources, "simple/vertex_shader.glsl")!!
    open fun fragmentShader() = getAssetAsString(resources, "simple/fragment_shader.glsl")!!
}