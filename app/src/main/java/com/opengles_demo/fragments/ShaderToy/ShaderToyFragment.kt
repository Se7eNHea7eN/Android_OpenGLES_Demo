package com.opengles_demo.fragments.ShaderToy

import android.content.pm.ActivityInfo
import android.opengl.GLES20.*
import android.os.Bundle
import asiainnovations.com.opengles_demo.GlShader
import asiainnovations.com.opengles_demo.fragments.BaseGLFragment
import asiainnovations.com.opengles_demo.getAssetAsString
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ShaderToyFragment : BaseGLFragment() {
    protected lateinit var shader: GlShader

    private var width: Int = 0
    private var height: Int = 0

    private var renderBeginTime = 0L
    private var lastFrameTime = 0L


    private var verticesHandler = 0
    private var globalTimeHandler = 0
    private var resolutionHandler = 0
    private var timeHandler = 0
    private var timeDeltaHandler = 0

    override val orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    private var vertices = floatArrayOf(-1f, -1f,
            -1f, 1f,
            1f, -1f,
            1f, 1f)

    private val vertexBuffer: FloatBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(vertices)
                position(0)
            }

    override fun onDrawFrame(gl: GL10?) {
        val thisFrameTime = System.currentTimeMillis()

        glClearColor(0f, 0f, 0f, 0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        if(verticesHandler >=0){
            glEnableVertexAttribArray(verticesHandler)
            glVertexAttribPointer(verticesHandler, 2, GL_FLOAT, false, 0, vertexBuffer)
        }
        if (globalTimeHandler >= 0)
            glUniform1f(globalTimeHandler, System.currentTimeMillis().toFloat())
        if(timeHandler >= 0)
            glUniform1f(timeHandler,(thisFrameTime - renderBeginTime).toFloat())
        if(timeDeltaHandler >= 0)
            glUniform1f(timeDeltaHandler,(thisFrameTime - lastFrameTime).toFloat())

        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)

        lastFrameTime = thisFrameTime
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height

        glClearDepthf(1.0f)
        glEnable(GL_CULL_FACE)
        glEnable(GL_DEPTH_TEST)
        glDepthFunc(GL_LEQUAL)
        glViewport(0, 0, width, height)

        if(resolutionHandler >= 0) {
            val param = floatArrayOf(width.toFloat(), height.toFloat(), 1f)

            glUniformMatrix3fv(resolutionHandler, 1, false,ByteBuffer.allocateDirect(param.size * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .apply {
                        put(param)
                        position(0)
                    } )
        }
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        shader = GlShader(vertexShader(), fragmentShader())

        globalTimeHandler = glGetUniformLocation(shader.program, "iGlobalTime")
        timeHandler = glGetUniformLocation(shader.program, "iTime")
        timeDeltaHandler = glGetUniformLocation(shader.program, "iTimeDelta")
        verticesHandler  = glGetAttribLocation(shader.program,"pos")
        resolutionHandler = glGetUniformLocation(shader.program, "iResolution")

        shader.useProgram()

        lastFrameTime = System.currentTimeMillis()
        renderBeginTime = System.currentTimeMillis()
    }

    companion object {
        fun newInstance(shaderName: String): ShaderToyFragment =
                ShaderToyFragment().apply {
                    arguments = Bundle().apply {
                        putString("shaderName", shaderName)
                    }
                }
    }



    private fun vertexShader() = getAssetAsString(resources, "shadertoy/vertex_shader.glsl")!!

    private fun fragmentShader(): String {
        return getAssetAsString(resources, "shadertoy/fragment_header.glsl")!! +
                fragmentImageShader() +
                getAssetAsString(resources, "shadertoy/fragment_footer.glsl")!!
    }

    private fun fragmentImageShader(): String {
        val shaderName = arguments!!.getString("shaderName")
        return getAssetAsString(resources, "shadertoy/${shaderName}.glsl")!!
    }
}