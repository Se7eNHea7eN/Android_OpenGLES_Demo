package asiainnovations.com.opengles_demo.fragments

import android.opengl.GLES20
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TestFragment : BaseGLFragment() {
    val BLOCK_WIDTH = 80
    val BLOCK_SPEED = 2
    var clearColor = 0.0f
    var xpos = -BLOCK_WIDTH / 2
    var xdir = BLOCK_SPEED
    var width : Int =0
    var height  : Int =0


    override fun onDrawFrame(p0: GL10?) {

        // Still alive, render a frame.
        GLES20.glClearColor(clearColor, clearColor, clearColor, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        GLES20.glEnable(GLES20.GL_SCISSOR_TEST)
        GLES20.glScissor(xpos, height / 4, BLOCK_WIDTH, height / 2)
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glDisable(GLES20.GL_SCISSOR_TEST)

        // Publish the frame.  If we overrun the consumer, frames will be dropped,
        // so on a sufficiently fast device the animation will run at faster than
        // the display refresh rate.
        //
        // If the SurfaceTexture has been destroyed, this will throw an exception.
        // Advance state
        clearColor += 0.015625f
        if (clearColor > 1.0f) {
            clearColor = 0.0f
        }
        xpos += xdir
        if (xpos <= -BLOCK_WIDTH / 2 || xpos >= width - BLOCK_WIDTH / 2) {
            xdir = -xdir
        }

    }

    override fun onSurfaceChanged(p0: GL10?, p1: Int, p2: Int) {
        width = p1
        height = p2
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
    }
}