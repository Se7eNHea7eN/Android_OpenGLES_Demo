package com.opengles_demo.fragments

import android.graphics.BitmapFactory
import android.opengl.GLES20.*
import android.opengl.Matrix
import asiainnovations.com.opengles_demo.getAssetAsString
import com.asiainnovations.onlyu.video.gl.GlUtil
import com.opengles_demo.R
import com.opengles_demo.gl.Cube
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TextureMappingFragment : MVPFragment() {
    var textureId: Int = 0

    private val vertexBuffer =
            ByteBuffer.allocateDirect(Cube.vertices.size * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .apply {
                        put(Cube.vertices)
                        position(0)
                    }
    private val textureMappingBuffer =
            ByteBuffer.allocateDirect(Cube.textureCoordinate.size * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .apply {
                        put(Cube.textureCoordinate)
                        position(0)
                    }

    override fun onEarlyDrawFrame() {
        Matrix.rotateM(modelMatrix, 0, 30 * deltaTime.toFloat() / 1000, 0.5f, 0.5f, 0.5f)
    }

    override fun onDrawFrame() {
        shader.useProgram()
        shader.setUniformMatrix("uMVPMatrix", mvpMatrix)
        shader.setVertexAttribArray("aPosition", 3, vertexBuffer)
        shader.setVertexAttribArray("aTextureCoordinate", 2, textureMappingBuffer)

        glDrawArrays(GL_TRIANGLES, 0, Cube.vertices.size / 3)
    }


    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        super.onSurfaceCreated(gl, config)
        textureId = GlUtil.createTextureFromBitmap(BitmapFactory.decodeResource(resources, R.mipmap.crate))
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, textureId)
        glUniform1i(shader.getUniformLocation("uTexture"), 0)
    }

    override fun vertexShader() = getAssetAsString(resources, "texture/vertex_shader.glsl")!!
    override fun fragmentShader() = getAssetAsString(resources, "texture/fragment_shader.glsl")!!
}