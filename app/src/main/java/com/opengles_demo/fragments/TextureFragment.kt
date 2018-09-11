package asiainnovations.com.opengles_demo.fragments

import android.graphics.BitmapFactory
import android.opengl.GLES20.*
import asiainnovations.com.opengles_demo.getAssetAsString
import com.asiainnovations.onlyu.video.gl.GlUtil
import com.asiainnovations.onlyu.video.gl.TextureRotationUtil
import com.opengles_demo.R
import com.opengles_demo.fragments.MVPFragment
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TextureFragment : MVPFragment() {
    var textureId: Int = 0

    private val vertexBuffer: FloatBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.CUBE.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(TextureRotationUtil.CUBE)
                position(0);
            }

    private val textureMappingBuffer: FloatBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_NO_ROTATION.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(TextureRotationUtil.getRotation(0, false, false))
                position(0)
            }

    override fun onDrawFrame() {
        shader.setVertexAttribArray("position", 2, vertexBuffer)
        shader.setVertexAttribArray("inputTextureCoordinate", 2, textureMappingBuffer)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)
    }


    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        super.onSurfaceCreated(gl, config)
        textureId = GlUtil.createTextureFromBitmap(BitmapFactory.decodeResource(resources, R.mipmap.bitmap01))
        //激活纹理单元0
        glActiveTexture(GL_TEXTURE0)
        //绑定纹理
        glBindTexture(GL_TEXTURE_2D, textureId)
        //设置到0号纹理单元
        glUniform1i(shader.getUniformLocation("inputImageTexture"), 0)
    }

    override fun vertexShader() = getAssetAsString(resources, "rect/vertex_shader.glsl")!!
    override fun fragmentShader() = getAssetAsString(resources, "rect/fragment_shader.glsl")!!
}