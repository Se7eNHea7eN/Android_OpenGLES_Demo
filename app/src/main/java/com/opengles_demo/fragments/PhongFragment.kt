package com.opengles_demo.fragments

import android.opengl.GLES20.*
import android.os.Bundle
import asiainnovations.com.opengles_demo.getAssetAsString
import com.opengles_demo.math.Vector3
import com.opengles_demo.obj.Obj
import com.opengles_demo.obj.ObjData
import com.opengles_demo.obj.ObjReader
import com.opengles_demo.obj.ObjUtils
import java.io.InputStreamReader
import java.nio.FloatBuffer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class PhongFragment : MVPFragment() {
    private var objColor = floatArrayOf(1f, 0f, 0f)
    private var lightColor = floatArrayOf(1f, 1f, 1f)
    private var ambientStrength = 0.1f
    private var lightPos = Vector3(3f, 3f, 3f)
    private var specularStrength = 1.5f
    private var shininess = 128f

    private lateinit var teapot: Obj
    private lateinit var teapotVertices: FloatBuffer
    private lateinit var teapotNormals: FloatBuffer
    private lateinit var teapotIndices: IntBuffer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inputStream = InputStreamReader(context!!.assets.open("models/teapot.obj"))
        teapot = ObjUtils.convertToRenderable(ObjReader.read(inputStream))
        teapotVertices = ObjData.getVertices(teapot)
        teapotNormals = ObjData.getNormals(teapot)
        teapotIndices = ObjData.getFaceVertexIndices(teapot)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        super.onSurfaceCreated(gl, config)
        shader.setVertexAttribArray("aPosition", 3, teapotVertices)
        shader.setVertexAttribArray("aNormal", 3, teapotNormals)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        eye.y = 8f
        eye.z = -8f
        super.onSurfaceChanged(gl, width, height)
    }

    override fun onEarlyDrawFrame() {
        lightPos = lightPos.rotate(Vector3(0f, 1f, 0f), 1f* deltaTime / 1000f * Math.PI.toFloat() / 2f)
    }

    override fun onDrawFrame() {
        shader.setUniform3fv("objColor", objColor)
        shader.setUniform3fv("lightColor", lightColor)
        shader.setUniform1fv("ambientStrength", ambientStrength)
        shader.setUniform3fv("lightPos", lightPos.asFloatArray())
        shader.setUniform3fv("eyePos", eye.asFloatArray())
        shader.setUniform1fv("specularStrength", specularStrength)
        shader.setUniform1fv("shininess", shininess)
        glDrawElements(GL_TRIANGLES, teapotIndices.capacity(), GL_UNSIGNED_INT, teapotIndices)
    }

    override fun vertexShader() = getAssetAsString(resources, "phong/vertex_shader.glsl")!!
    override fun fragmentShader() = getAssetAsString(resources, "phong/fragment_shader.glsl")!!
}