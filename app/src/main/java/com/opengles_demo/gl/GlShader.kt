package asiainnovations.com.opengles_demo

import android.opengl.GLES20.*
import android.util.Log
import com.asiainnovations.onlyu.video.gl.GlUtil
import java.nio.FloatBuffer

// Helper class for handling OpenGL shaders and shader programs.
class GlShader {

    public var program: Int = 0

    constructor(vertexSource: String, fragmentSource: String) {
        val vertexShader = compileShader(GL_VERTEX_SHADER, vertexSource)
        val fragmentShader = compileShader(GL_FRAGMENT_SHADER, fragmentSource)
        program = glCreateProgram()
        if (program == 0) {
            throw RuntimeException("glCreateProgram() failed. GLES20 error: " + glGetError())
        }
        glAttachShader(program, vertexShader)
        glAttachShader(program, fragmentShader)
        glLinkProgram(program)
        val linkStatus = intArrayOf(GL_FALSE)
        glGetProgramiv(program, GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] != GL_TRUE) {
            Log.e(TAG, "Could not link program: " + glGetProgramInfoLog(program))
            throw RuntimeException(glGetProgramInfoLog(program))
        }
        // According to the documentation of glLinkProgram():
        // "After the link operation, applications are free to modify attached shader objects, compile
        // attached shader objects, detach shader objects, delete shader objects, and attach additional
        // shader objects. None of these operations affects the information log or the program that is
        // part of the program object."
        // But in practice, detaching shaders from the program seems to break some devices. Deleting the
        // shaders are fine however - it will delete them when they are no longer attached to a program.
        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)
        GlUtil.checkNoGLES2Error("Creating GlShader")
    }

    constructor(vertexShader:Int,fragmentShader:Int){
        program = glCreateProgram()
        if (program == 0) {
            throw RuntimeException("glCreateProgram() failed. GLES20 error: " + glGetError())
        }
        glAttachShader(program, vertexShader)
        glAttachShader(program, fragmentShader)
        glLinkProgram(program)
        val linkStatus = intArrayOf(GL_FALSE)
        glGetProgramiv(program, GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] != GL_TRUE) {
            Log.e(TAG, "Could not link program: " + glGetProgramInfoLog(program))
            throw RuntimeException(glGetProgramInfoLog(program))
        }
        GlUtil.checkNoGLES2Error("Creating GlShader")
    }

    fun getAttribLocation(label: String): Int {
        if (program == -1) {
            throw RuntimeException("The program has been released")
        }
        val location = glGetAttribLocation(program, label)
        if (location < 0) {
            throw RuntimeException("Could not locate '$label' in program")
        }
        return location
    }

    /**
     * Enable and upload a vertex array for attribute |label|. The vertex data is specified in
     * |buffer| with |dimension| number of components per vertex.
     */
    fun setVertexAttribArray(label: String, dimension: Int, buffer: FloatBuffer): Int {
        if (program == -1) {
            throw RuntimeException("The program has been released")
        }
        val location = getAttribLocation(label)
        glEnableVertexAttribArray(location)
        glVertexAttribPointer(location, dimension, GL_FLOAT, false, 0, buffer)
        GlUtil.checkNoGLES2Error("setVertexAttribArray")
        return location
    }

    fun setUniformMatrix3fv(label: String, matrix: FloatArray) : Int{
        if (program == -1) {
            throw RuntimeException("The program has been released")
        }

        val location = getUniformLocation(label)
        glUniformMatrix3fv(location, 1, false, matrix, 0)
        GlUtil.checkNoGLES2Error("glUniformMatrix3fv")
        return location
    }

    fun setUniformMatrix4fv(label: String, matrix: FloatArray) : Int{
        if (program == -1) {
            throw RuntimeException("The program has been released")
        }

        val location = getUniformLocation(label)
        glUniformMatrix4fv(location, 1, false, matrix, 0)
        GlUtil.checkNoGLES2Error("glUniformMatrix4fv")
        return location
    }

    fun setUniformFloat(label: String,value: Float) : Int{
        if (program == -1) {
            throw RuntimeException("The program has been released")
        }

        val location = getUniformLocation(label)
        glUniform1f(location,value)
        GlUtil.checkNoGLES2Error("glUniformMatrix4fv")
        return location
    }
    fun getUniformLocation(label: String): Int {
        if (program == -1) {
            throw RuntimeException("The program has been released")
        }
        val location = glGetUniformLocation(program, label)
        if (location < 0) {
            throw RuntimeException("Could not locate uniform '$label' in program")
        }
        return location
    }

    fun useProgram() {
        if (program == -1) {
            throw RuntimeException("The program has been released")
        }
        glUseProgram(program)
        GlUtil.checkNoGLES2Error("glUseProgram")
    }

    fun release() {
        Log.d(TAG, "Deleting shader.")
        // Delete program, automatically detaching any shaders from it.
        if (program != -1) {
            glDeleteProgram(program)
            program = -1
        }
    }

    companion object {
        private val TAG = "GlShader"

        private fun compileShader(shaderType: Int, source: String): Int {
            val shader = glCreateShader(shaderType)
            if (shader == 0) {
                throw RuntimeException("glCreateShader() failed. GLES20 error: " + glGetError())
            }
            glShaderSource(shader, source)
            glCompileShader(shader)
            val compileStatus = intArrayOf(GL_FALSE)
            glGetShaderiv(shader, GL_COMPILE_STATUS, compileStatus, 0)
            if (compileStatus[0] != GL_TRUE) {
                Log.e(TAG, "Could not compile shader " + shaderType + ":" + glGetShaderInfoLog(shader))
                throw RuntimeException(glGetShaderInfoLog(shader))
            }
            GlUtil.checkNoGLES2Error("compileShader")
            return shader
        }
    }
}