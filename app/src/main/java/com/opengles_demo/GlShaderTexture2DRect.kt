package asiainnovations.com.opengles_demo

import android.opengl.GLES20.*
import java.nio.FloatBuffer

/**
 * Author: zihao.Liu(lzh123@qq.com)
 * 通用Texture2D着色器
 */
class GlShaderTexture2DRect {
    private val glShader: GlShader

    init {
        glShader = GlShader(CAMERA_INPUT_VERTEX_SHADER, CAMERA_INPUT_FRAGMENT_SHADER)
    }

    fun setVertexPosition(vertexBuffer: FloatBuffer) {
        vertexBuffer.position(0)
        glShader.setVertexAttribArray(POSITION_COORDINATE, 2, vertexBuffer)
    }

    fun setTextureCoordinate(textureBuffer: FloatBuffer) {
        textureBuffer.position(0)
        glShader.setVertexAttribArray(TEXTURE_COORDINATE, 2, textureBuffer)
    }

    fun setImageTexture(textureId: Int) {
        if (textureId != -1) {
            glActiveTexture(GL_TEXTURE0)
            glBindTexture(GL_TEXTURE_2D, textureId)

            glUniform1i(glShader.getUniformLocation(TEXTURE_UNIFORM), 0)
        }
    }

    fun draw() {
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)
    }

    fun disable() {
        glDisableVertexAttribArray(glShader.getAttribLocation(POSITION_COORDINATE))
        glDisableVertexAttribArray(glShader.getAttribLocation(TEXTURE_COORDINATE))
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, 0)
        glUseProgram(0)
    }

    fun useProgram() {
        glShader.useProgram()
    }


    fun release() {
        glShader.release()
    }

    companion object {
        private val CAMERA_INPUT_VERTEX_SHADER = "" +
                "attribute vec4 position;\n" +
                "attribute vec4 inputTextureCoordinate;\n" +
                "\n" +
                "varying vec2 textureCoordinate;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "	textureCoordinate = inputTextureCoordinate.xy;\n" +
                "	gl_Position = position;\n" +
                "}"
        private val CAMERA_INPUT_FRAGMENT_SHADER = "" +
                "precision mediump float;\n" +
                "varying highp vec2 textureCoordinate;\n" +
                " \n" +
                "uniform sampler2D inputImageTexture;\n" +
                " \n" +
                "void main()\n" +
                "{\n" +
                "     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "}"


        private val POSITION_COORDINATE = "position"
        private val TEXTURE_UNIFORM = "inputImageTexture"
        private val TEXTURE_COORDINATE = "inputTextureCoordinate"
    }
}
