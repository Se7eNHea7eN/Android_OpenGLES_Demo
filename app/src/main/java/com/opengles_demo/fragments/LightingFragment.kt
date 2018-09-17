package com.opengles_demo.fragments

import android.graphics.BitmapFactory
import android.opengl.GLES20.*
import android.opengl.Matrix
import asiainnovations.com.opengles_demo.GlShader
import asiainnovations.com.opengles_demo.getAssetAsString
import com.asiainnovations.onlyu.video.gl.GlUtil
import com.opengles_demo.R
import com.opengles_demo.gl.Cube
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class LightingFragment : MVPFragment() {

    var textureId: Int = 0
    protected val lightModelMatrix = FloatArray(16)

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

    private val normalBuffer =
            ByteBuffer.allocateDirect(Cube.cubeNormalData.size * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .apply {
                        put(Cube.cubeNormalData)
                        position(0)
                    }

    private lateinit var lightShader: GlShader

    /** Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
     * we multiply this by our transformation matrices.  */
    private val mLightPosInModelSpace = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)

    /** Used to hold the current position of the light in world space (after transformation via model matrix).  */
    private val mLightPosInWorldSpace = FloatArray(4)

    /** Used to hold the transformed position of the light in eye space (after transformation via modelview matrix)  */
    private val mLightPosInEyeSpace = FloatArray(4)
    var angleInDegrees = 0f

    override fun onEarlyDrawFrame() {
        Matrix.rotateM(modelMatrix, 0, 30 * deltaTime.toFloat() / 1000, 0.5f, 0.5f, 0.5f)

        Matrix.setIdentityM(lightModelMatrix, 0)
        Matrix.translateM(lightModelMatrix, 0, 0.0f, 0.0f, -1.0f)
        angleInDegrees -= 0.1f* deltaTime
        Matrix.rotateM(lightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f)
        Matrix.translateM(lightModelMatrix, 0, 0.0f, 0.0f, 2f)

        Matrix.multiplyMV(mLightPosInWorldSpace, 0, lightModelMatrix, 0, mLightPosInModelSpace, 0)
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, viewMatrix, 0, mLightPosInWorldSpace, 0)
    }

    override fun onDrawFrame() {
        shader.useProgram()
        shader.setUniformMatrix("uMVPMatrix", mvpMatrix)
        shader.setUniformMatrix("uMVMatrix", mvMatrix)
        shader.setVertexAttribArray("aPosition", 3, vertexBuffer)
        shader.setVertexAttribArray("aNormal",3,normalBuffer)
        shader.setVertexAttribArray("aTextureCoordinate", 2, textureMappingBuffer)
        glUniform3f(shader.getUniformLocation("uLightPos"),  mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2])

        glDrawArrays(GL_TRIANGLES, 0, Cube.vertices.size / 3)

        lightShader.useProgram()
        glVertexAttrib3f(lightShader.getAttribLocation("aPosition"), mLightPosInModelSpace[0], mLightPosInModelSpace[1], mLightPosInModelSpace[2])

        val tempMVMatrix  = FloatArray(16)
        Matrix.multiplyMM(tempMVMatrix, 0, viewMatrix, 0, lightModelMatrix, 0)
        val tempMVPMatrixM = FloatArray(16)
        Matrix.multiplyMM(tempMVPMatrixM, 0, projectionMatrix, 0, tempMVMatrix, 0)
        lightShader.setUniformMatrix("uMVPMatrix", tempMVPMatrixM)

        glDrawArrays(GL_POINTS, 0, 1)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        eyeZ = -8f
        super.onSurfaceChanged(gl, width, height)
        Matrix.setIdentityM(lightModelMatrix, 0)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        super.onSurfaceCreated(gl, config)
        textureId = GlUtil.createTextureFromBitmap(BitmapFactory.decodeResource(resources, R.mipmap.crate))

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, textureId)
        glUniform1i(shader.getUniformLocation("uTexture"), 0)

        lightShader = GlShader(getAssetAsString(resources, "point/vertex_shader.glsl")!!,
                getAssetAsString(resources, "point/fragment_shader.glsl")!!)
    }

    override fun vertexShader() = getAssetAsString(resources, "lighting/vertex_shader.glsl")!!

    override fun fragmentShader() = getAssetAsString(resources, "lighting/fragment_shader.glsl")!!
}