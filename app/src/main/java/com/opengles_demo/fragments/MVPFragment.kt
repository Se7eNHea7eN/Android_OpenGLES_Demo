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
    protected val projectionMatrix = FloatArray(16)
    //模型矩阵
    protected val modelMatrix = FloatArray(16)
    //视矩阵
    protected val viewMatrix = FloatArray(16)

    protected val mvMatrix = FloatArray(16)

    //透视矩阵与视图矩阵变换后的总矩阵
    protected val mvpMatrix = FloatArray(16)

    protected lateinit var shader: GlShader
    private var lastDrawTime: Long = 0
    protected var deltaTime = 0L


    // Position the eye in front of the origin.
    protected var eyeX = 0.0f
    protected var eyeY = 0.0f
    protected var eyeZ = -5f

     // We are looking toward the distance
    protected var lookX = 0.0f
    protected var lookY = 0.0f
    protected var lookZ = 1.0f

    // Set our up vector. This is where our head would be pointing were we holding the camera.
    protected var upX = 0.0f
    protected var upY = 1.0f
    protected var upZ = 0.0f

    final override fun onDrawFrame(gl: GL10) {
        val time = System.currentTimeMillis()
        deltaTime = time - lastDrawTime
        lastDrawTime = time
        onEarlyDrawFrame()
        Matrix.multiplyMM(mvMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvMatrix, 0)
        glClearColor(0f, 0f, 0f, 0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        onDrawFrame()
    }

    open fun onEarlyDrawFrame(){}

    abstract fun onDrawFrame()

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        glClearDepthf(1.0f)
        glEnable(GL_CULL_FACE)
        glEnable(GL_DEPTH_TEST)
        glDepthFunc(GL_LEQUAL)
        glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 20f);

        Matrix.setIdentityM(modelMatrix, 0)


        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        lastDrawTime = System.currentTimeMillis()
        shader = GlShader(vertexShader(),fragmentShader())
        shader.useProgram()
    }

    open fun vertexShader() = getAssetAsString(resources, "simple/vertex_shader.glsl")!!
    open fun fragmentShader() = getAssetAsString(resources, "simple/fragment_shader.glsl")!!
}