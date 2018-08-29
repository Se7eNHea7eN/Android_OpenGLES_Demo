package asiainnovations.com.opengles_demo.fragments

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLES10Fragment : BaseGLFragment() {
    protected var ratio: Float = 1f

    override fun getEglContextClientVersion(): Int = 1

    private lateinit var vertexBuffer: FloatBuffer  // Buffer for vertex-array
    private lateinit var indexBuffer: ByteBuffer    // Buffer for index-array
    private lateinit var colorBuffer: FloatBuffer   // Buffer for color-array (NEW)

    private val vertices = floatArrayOf(// Vertices of the triangle
            0.0f, 0.577350269f, 0f,  // 0 top
            -0.5f, -0.288675135f, 0f, // 1 bottom left
            0.5f, -0.288675135f, 0f // 2 bottom right
    )
    private val indices = byteArrayOf(0, 1, 2) // Indices to above vertices (in CCW)
    private val colors = floatArrayOf(// Colors for the vertices (NEW)
            1.0f, 0.0f, 0.0f, 1.0f, // Red (NEW)
            0.0f, 1.0f, 0.0f, 1.0f, // Green (NEW)
            0.0f, 0.0f, 1.0f, 1.0f  // Blue (NEW)
    )

    override fun onDrawFrame(gl: GL10) {
        gl.glClearColor(0f, 0f, 0f, 0f)
        gl.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        // Enable vertex-array and define the buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer)
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY)          // Enable color-array (NEW)
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer)  // Define color-array buffer (NEW)
        // Draw the primitives via index-array
        gl.glDrawElements(GL10.GL_TRIANGLES, indices.size, GL10.GL_UNSIGNED_BYTE, indexBuffer)
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY)   // Disable color-array (NEW)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        ratio = width.toFloat() / height
        gl.glLoadIdentity()
        gl.glScalef(1f, ratio, 1f)
    }


    override fun onSurfaceCreated(gl: GL10, p1: EGLConfig?) {
        // Setup vertex-array buffer. Vertices in float. A float has 4 bytes.

        val vbb = ByteBuffer.allocateDirect(vertices.size * 4)
        vbb.order(ByteOrder.nativeOrder()) // Use native byte order
        vertexBuffer = vbb.asFloatBuffer() // Convert byte buffer to float
        vertexBuffer.put(vertices)         // Copy data into buffer
        vertexBuffer.position(0)           // Rewind
        // Setup color-array buffer. Colors in float. A float has 4 bytes (NEW)
        val cbb = ByteBuffer.allocateDirect(colors.size * 4)
        cbb.order(ByteOrder.nativeOrder()) // Use native byte order (NEW)
        colorBuffer = cbb.asFloatBuffer()  // Convert byte buffer to float (NEW)
        colorBuffer.put(colors)            // Copy data into buffer (NEW)
        colorBuffer.position(0)            // Rewind (NEW)

        // Setup index-array buffer. Indices in byte.
        indexBuffer = ByteBuffer.allocateDirect(indices.size)
        indexBuffer.put(indices)
        indexBuffer.position(0)
    }
}