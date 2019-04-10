package asiainnovations.com.opengles_demo.fragments

import android.opengl.GLES20.GL_TRIANGLES
import android.opengl.GLES20.glDrawArrays
import android.opengl.Matrix
import com.opengles_demo.fragments.MVPFragment
import com.opengles_demo.gl.Cube
import java.nio.ByteBuffer
import java.nio.ByteOrder

class CubeFragment : MVPFragment() {
    override fun onEarlyDrawFrame() {
        Matrix.rotateM(modelMatrix, 0, 30 * deltaTime.toFloat() / 1000, 0.5f, 0.5f, 0.5f)
    }

    override fun onDrawFrame() {

        shader.setVertexAttribArray("aPosition", 3, ByteBuffer.allocateDirect(Cube.vertices.size * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer().apply {
                    put(Cube.vertices)
                    position(0)
                })
        shader.setVertexAttribArray("aColor", 4, ByteBuffer.allocateDirect(Cube.colors.size * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer().apply {
                    put(Cube.colors)
                    position(0)
                })

        glDrawArrays(GL_TRIANGLES, 0, Cube.vertices.size / 3)
    }
}