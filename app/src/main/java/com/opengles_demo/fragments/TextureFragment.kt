package asiainnovations.com.opengles_demo.fragments

import android.graphics.BitmapFactory
import android.opengl.GLES20.*
import asiainnovations.com.opengles_demo.GlShader
import asiainnovations.com.opengles_demo.getAssetAsString
import com.asiainnovations.onlyu.video.gl.GlUtil
import com.asiainnovations.onlyu.video.gl.TextureRotationUtil
import com.opengles_demo.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TextureFragment : BaseGLFragment() {
    var textureId: Int = 0
    var width: Int = 0
    var height: Int = 0
    private lateinit var shader: GlShader

    private val vertexBuffer: FloatBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.CUBE.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(TextureRotationUtil.CUBE).position(0);
            }

    private val textureMappingBuffer: FloatBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_NO_ROTATION.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(TextureRotationUtil.getRotation(0, false, false)).position(0)
            }

    override fun onDrawFrame(p0: GL10?) {
        glClearColor(0f, 0f, 0f, 0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        shader.useProgram()

        vertexBuffer.position(0)
        shader.setVertexAttribArray("position", 2, vertexBuffer)
        textureMappingBuffer.position(0)
        shader.setVertexAttribArray("inputTextureCoordinate", 2, textureMappingBuffer)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, textureId)

        glUniform1i(shader.getUniformLocation("inputImageTexture"), 0)

        glViewport(0, 0, width, height)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)

        glDisableVertexAttribArray(shader.getAttribLocation("position"))
        glDisableVertexAttribArray(shader.getAttribLocation("inputTextureCoordinate"))
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, 0)
        glUseProgram(0)
    }

    override fun onSurfaceChanged(p0: GL10?, p1: Int, p2: Int) {
        width = p1
        height = p2
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        textureId = GlUtil.createTextureFromBitmap(BitmapFactory.decodeResource(resources, R.mipmap.bitmap01))

        vertexBuffer.put(TextureRotationUtil.CUBE).position(0)

        shader = GlShader(
                getAssetAsString(resources, "rect/vertex_shader.glsl")!!,
                getAssetAsString(resources, "rect/fragment_shader.glsl")!!
        )
    }
}