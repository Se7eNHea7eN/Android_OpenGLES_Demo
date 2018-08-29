package asiainnovations.com.opengles_demo.fragments

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import asiainnovations.com.opengles_demo.GlShader
import asiainnovations.com.opengles_demo.getAssetAsString
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CustomizeShaderFragment : BaseGLFragment() {

    private val TRIANGLE_COORDS = floatArrayOf(
            0.0f, 0.577350269f,  // 0 top
            -0.5f, -0.288675135f, // 1 bottom left
            0.5f, -0.288675135f // 2 bottom right
    )

    private val colors = floatArrayOf(// Colors for the vertices (NEW)
            1.0f, 0.0f, 0.0f, 1.0f, // Red (NEW)
            0.0f, 1.0f, 0.0f, 1.0f, // Green (NEW)
            0.0f, 0.0f, 1.0f, 1.0f  // Blue (NEW)
    )
    private var lastDrawTime: Long = 0

    //透视矩阵
    private val mProjMatrix = FloatArray(16)
    //视图矩阵
    private val mVMatrix = FloatArray(16)
    //透视矩阵与视图矩阵变换后的总矩阵
    private val mMVPMatrix = FloatArray(16)

    override fun getRenderMode(): Int = GLSurfaceView.RENDERMODE_CONTINUOUSLY

    val vertexBuffer = ByteBuffer.allocateDirect(TRIANGLE_COORDS.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(TRIANGLE_COORDS)
                position(0)
            }
    val colorBuffer = ByteBuffer.allocateDirect(colors.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(colors)
                position(0)
            }
    private lateinit var shader: GlShader

    override fun onDrawFrame(gl: GL10) {
        glClearColor(0f, 0f, 0f, 0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        shader.useProgram()
        colorBuffer.position(0)
        shader.setVertexAttribArray("aColor", 4, colorBuffer)

        val time = System.currentTimeMillis()
        Matrix.rotateM(mVMatrix,0,30f* (time - lastDrawTime).toFloat() / 1000,0f,0f,1f )
        lastDrawTime = time

        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

        shader.setUniformMatrix("uMVPMatrix", mMVPMatrix)
        shader.setVertexAttribArray("aPosition", 2, vertexBuffer)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 3)
        glDisableVertexAttribArray(shader.getAttribLocation("aPosition"))
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, 0)
        glUseProgram(0)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        val ratio = width.toFloat()/height

        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 7f);

        mVMatrix.apply {
            Matrix.setIdentityM(this,0)
            Matrix.translateM(this,0,0f,0.2f,-2f)
        }
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        lastDrawTime = System.currentTimeMillis()

        shader = GlShader(
                getAssetAsString(resources, "vertex_shader.glsl")!!,
                getAssetAsString(resources, "fragment_shader.glsl")!!
        )
    }

}