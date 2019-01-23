package com.opengles_demo.fragments

import android.content.pm.ActivityInfo
import android.opengl.GLES20.*
import android.os.Bundle
import asiainnovations.com.opengles_demo.GlShader
import asiainnovations.com.opengles_demo.fragments.BaseGLFragment
import asiainnovations.com.opengles_demo.getAssetAsString
import com.asiainnovations.onlyu.video.gl.TextureRotationUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ShaderToyFragment : BaseGLFragment() {
    companion object {
        fun newInstance(shaderName: String): ShaderToyFragment =
                ShaderToyFragment().apply {
                    arguments = Bundle().apply {
                        putString("shaderName", shaderName)
                    }
                }
    }

    protected lateinit var shader: GlShader

    private var renderBeginTime = 0L
    private var lastFrameTime = 0L


    private var verticesHandler = 0
    private var globalTimeHandler = 0
    private var resolutionHandler = 0
    private var timeHandler = 0
    private var timeDeltaHandler = 0
    private var frameHandler = 0


    private var surfaceWidth = 0
    private var surfaceHeight = 0

    private var frameCount = 0
    override val orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    private val vertexBuffer: FloatBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.CUBE.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(TextureRotationUtil.CUBE)
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
            glUniform1f(globalTimeHandler, (thisFrameTime - renderBeginTime) /1000f)
        if(timeHandler >= 0)
            glUniform1f(timeHandler,(thisFrameTime - renderBeginTime) /1000f)
        if(timeDeltaHandler >= 0)
            glUniform1f(timeDeltaHandler,(thisFrameTime - lastFrameTime)/1000f)
        if(frameHandler >= 0)
            glUniform1i(frameHandler,frameCount++)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)

        lastFrameTime = thisFrameTime
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glClearDepthf(1.0f)
        glEnable(GL_CULL_FACE)
        glEnable(GL_DEPTH_TEST)
        glDepthFunc(GL_LEQUAL)
        //glViewport(0, 0, width, height)
        surfaceWidth = width
        surfaceHeight = height

        if(resolutionHandler >= 0) {
            val param = floatArrayOf(surfaceWidth.toFloat(), surfaceHeight.toFloat(), 1f)
            glUniform3fv(resolutionHandler,1,param,0)
        }
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        shader = GlShader(vertexShader(), fragmentShader())

        globalTimeHandler = glGetUniformLocation(shader.program, "iGlobalTime")
        timeHandler = glGetUniformLocation(shader.program, "iTime")
        timeDeltaHandler = glGetUniformLocation(shader.program, "iTimeDelta")
        verticesHandler  = glGetAttribLocation(shader.program,"pos")
        resolutionHandler = glGetUniformLocation(shader.program, "iResolution")
        frameHandler = glGetUniformLocation(shader.program,"iFrame")

        shader.useProgram()

        lastFrameTime = System.currentTimeMillis()
        renderBeginTime = System.currentTimeMillis()
    }

    private fun vertexShader() = getAssetAsString(resources, "shadertoy/vertex_shader.glsl")!!

    private fun fragmentShader(): String {
        return getAssetAsString(resources, "shadertoy/fragment_header.glsl")!! +
                '\n' +
                fragmentImageShader() +
                '\n' +
                getAssetAsString(resources, "shadertoy/fragment_footer.glsl")!!
    }

    private fun fragmentImageShader(): String {
        val shaderName = arguments!!.getString("shaderName")
        return getAssetAsString(resources, "shadertoy/${shaderName}.glsl")!!
    }
}