package asiainnovations.com.opengles_demo.fragments

import android.opengl.GLES20.*
import asiainnovations.com.opengles_demo.GlShader
import asiainnovations.com.opengles_demo.getAssetAsString
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CustomizShaderFragment : BaseGLFragment() {
    private val TRIANGLE_COORDS = floatArrayOf(
            0.0f, 0.577350269f,  // 0 top
            -0.5f, -0.288675135f, // 1 bottom left
            0.5f, -0.288675135f // 2 bottom right
    )
    private val Matrix = floatArrayOf(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
    )

    val vertexBuffer = ByteBuffer.allocateDirect(TRIANGLE_COORDS.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(TRIANGLE_COORDS)
                position(0)
            }

    private lateinit var shader: GlShader

    override fun onDrawFrame(gl: GL10) {
        glClearColor(1f, 1f, 1f, 1f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        shader.useProgram()

        glUniform4fv(shader.getUniformLocation("uColor"), 1, floatArrayOf(1f, 0f, 0f, 1f), 0)
        shader.setUniformMatrix("uMVPMatrix", Matrix)
        shader.setVertexAttribArray("aPosition", 2, vertexBuffer)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 3)
        glDisableVertexAttribArray(shader.getAttribLocation("aPosition"))
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, 0)
        glUseProgram(0)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        shader = GlShader(
                getAssetAsString(resources, "vertex_shader.glsl")!!,
                getAssetAsString(resources, "fragment_shader.glsl")!!
        )
    }

}