package asiainnovations.com.opengles_demo.fragments

import android.graphics.BitmapFactory
import android.opengl.GLES20.*
import asiainnovations.com.opengles_demo.GlShaderTexture2DRect
import com.asiainnovations.onlyu.video.gl.GlUtil
import com.asiainnovations.onlyu.video.gl.TextureRotationUtil
import com.opengles_demo.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TextureFragment : BaseGLFragment() {
    lateinit var glShaderTexture2DRect:GlShaderTexture2DRect
    var textureId:Int = 0
    var width : Int =0
    var height  : Int =0
    private val mGLCubeBuffer: FloatBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.CUBE.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(TextureRotationUtil.CUBE).position(0);
            }

    private val mGLTextureBuffer: FloatBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_NO_ROTATION.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(TextureRotationUtil.getRotation(0, false, false)).position(0)
            }

    override fun onDrawFrame(p0: GL10?) {
        glClearColor(0f, 0f, 0f, 0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        glShaderTexture2DRect.useProgram()
        GlUtil.checkNoGLES2Error("glUseProgram")

        glShaderTexture2DRect.setVertexPosition(mGLCubeBuffer)
        glShaderTexture2DRect.setTextureCoordinate(mGLTextureBuffer)
        glShaderTexture2DRect.setImageTexture(textureId)

        glViewport(0, 0, width, height)

        glShaderTexture2DRect.draw()

        glBindTexture(GL_TEXTURE_2D, 0)

        glShaderTexture2DRect.disable()

    }

    override fun onSurfaceChanged(p0: GL10?, p1: Int, p2: Int) {
        width = p1
        height = p2
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        textureId = GlUtil.createTextureFromBitmap(BitmapFactory.decodeResource(resources, R.mipmap.bitmap01))
        glShaderTexture2DRect = GlShaderTexture2DRect()

        mGLCubeBuffer.put(TextureRotationUtil.CUBE).position(0)
    }
}