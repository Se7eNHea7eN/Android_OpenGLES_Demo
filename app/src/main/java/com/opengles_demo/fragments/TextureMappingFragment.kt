package com.opengles_demo.fragments

import android.graphics.BitmapFactory
import android.opengl.GLES20.*
import android.opengl.Matrix
import asiainnovations.com.opengles_demo.GlShader
import asiainnovations.com.opengles_demo.fragments.BaseGLFragment
import asiainnovations.com.opengles_demo.getAssetAsString
import com.asiainnovations.onlyu.video.gl.GlUtil
import com.opengles_demo.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TextureMappingFragment : BaseGLFragment() {
    private lateinit var shader: GlShader
    private var lastDrawTime: Long = 0
    var textureId: Int = 0

    //透视矩阵
    private val mProjMatrix = FloatArray(16)
    //视图矩阵
    private val mVMatrix = FloatArray(16)
    //透视矩阵与视图矩阵变换后的总矩阵
    private val mMVPMatrix = FloatArray(16)

    private val textureCoordinates = floatArrayOf(
            0.0f, 0.0f,//左下
            1.0f, 0.0f,//右下
            0.0f, 1.0f,//左上
            0.0f, 1.0f,//左上
            1.0f, 0.0f,//右下
            1.0f, 1.0f,//右上

            1.0f, 0.0f,//右下
            1.0f, 1.0f,//右上
            0.0f, 0.0f,//左下
            0.0f, 0.0f,//左下
            0.0f, 1.0f,//左上
            1.0f, 1.0f,//右上

            1.0f, 0.0f,//右下
            0.0f, 0.0f,//左下
            0.0f, 1.0f,//左上
            0.0f, 1.0f,//左上
            1.0f, 1.0f,//右上
            1.0f, 0.0f,//右下

            1.0f, 0.0f,//右下
            0.0f, 0.0f,//左下
            0.0f, 1.0f,//左上
            0.0f, 1.0f,//左上
            1.0f, 1.0f,//右上
            1.0f, 0.0f,//右下

            0.0f, 1.0f,//左上
            1.0f, 1.0f,//右上
            1.0f, 0.0f,//右下
            1.0f, 0.0f,//右下
            0.0f, 0.0f,//左下
            0.0f, 1.0f,//左上

            0.0f, 1.0f,//左上
            1.0f, 1.0f,//右上
            1.0f, 0.0f,//右下
            1.0f, 0.0f,//右下
            0.0f, 0.0f,//左下
            0.0f, 1.0f//左上
    )



    private val vertices = floatArrayOf(// Vertices of the 6 faces
            // FRONT
            -1.0f, -1.0f, 1.0f,  // 0. left-bottom-front
            1.0f, -1.0f, 1.0f,  // 1. right-bottom-front
            -1.0f, 1.0f, 1.0f,  // 2. left-top-front
            -1.0f, 1.0f, 1.0f,  // 2. left-top-front
            1.0f, -1.0f, 1.0f,  // 1. right-bottom-front
            1.0f, 1.0f, 1.0f,  // 3. right-top-front
            // BACK
            1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            1.0f, 1.0f, -1.0f,  // 7. right-top-back
            -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            -1.0f, 1.0f, -1.0f,  // 5. left-top-back
            1.0f, 1.0f, -1.0f,  // 7. right-top-back
            // LEFT
            -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            -1.0f, -1.0f, 1.0f,  // 0. left-bottom-front
            -1.0f, 1.0f, 1.0f,  // 2. left-top-front
            -1.0f, 1.0f, 1.0f,  // 2. left-top-front
            -1.0f, 1.0f, -1.0f,  // 5. left-top-back
            -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            // RIGHT
            1.0f, -1.0f, 1.0f,  // 1. right-bottom-front
            1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            1.0f, 1.0f, -1.0f,  // 7. right-top-back
            1.0f, 1.0f, -1.0f,  // 7. right-top-back
            1.0f, 1.0f, 1.0f,  // 3. right-top-front
            1.0f, -1.0f, 1.0f,  // 1. right-bottom-front
            // TOP
            -1.0f, 1.0f, 1.0f,  // 2. left-top-front
            1.0f, 1.0f, 1.0f,  // 3. right-top-front
            1.0f, 1.0f, -1.0f,  // 7. right-top-back
            1.0f, 1.0f, -1.0f,  // 7. right-top-back
            -1.0f, 1.0f, -1.0f,  // 5. left-top-back
            -1.0f, 1.0f, 1.0f,  // 2. left-top-front
            // BOTTOM
            -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            1.0f, -1.0f, 1.0f,   // 1. right-bottom-front
            1.0f, -1.0f, 1.0f,   // 1. right-bottom-front
            -1.0f, -1.0f, 1.0f,  // 0. left-bottom-front
            -1.0f, -1.0f, -1.0f  // 4. left-bottom-back
    )
    private val textureMappingBuffer: FloatBuffer = ByteBuffer.allocateDirect(textureCoordinates.size* 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(textureCoordinates)
            }
    override fun onDrawFrame(gl: GL10) {
        glClearColor(0f, 0f, 0f, 0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        shader.useProgram()
        val time = System.currentTimeMillis()

        Matrix.rotateM(mVMatrix, 0, 30 * (time - lastDrawTime).toFloat() / 1000, 0.5f, 0.5f, 0.5f)
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
        lastDrawTime = time

        shader.setUniformMatrix("uMVPMatrix", mMVPMatrix)

        shader.setVertexAttribArray("aPosition", 3, ByteBuffer.allocateDirect(vertices.size * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer().apply {
                    put(vertices)
                    position(0)
                })

        textureMappingBuffer.position(0)
        shader.setVertexAttribArray("aTextureCoordinate", 2, textureMappingBuffer)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, textureId)

        glUniform1i(shader.getUniformLocation("uTexture"), 0)

        glDrawArrays(GL_TRIANGLES, 0, vertices.size / 3)


        glDisableVertexAttribArray(shader.getAttribLocation("aPosition"))
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, 0)
        glUseProgram(0)
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        glClearDepthf(1.0f)
        glEnable(GL_DEPTH_TEST)
        glDepthFunc(GL_LEQUAL)

        val ratio = width.toFloat() / height
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 7f);


        mVMatrix.apply {
            Matrix.setIdentityM(this, 0)
            Matrix.translateM(this, 0, 0f, 0f, -2f)
            Matrix.scaleM(this, 0, 0.5f, 0.5f, 0.5f)
        }
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        lastDrawTime = System.currentTimeMillis()
        textureId = GlUtil.createTextureFromBitmap(BitmapFactory.decodeResource(resources, R.mipmap.bitmap01))

        shader = GlShader(
                getAssetAsString(resources, "texture/vertex_shader.glsl")!!,
                getAssetAsString(resources, "texture/fragment_shader.glsl")!!
        )

    }
}