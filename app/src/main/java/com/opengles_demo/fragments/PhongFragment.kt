package com.opengles_demo.fragments

import android.opengl.GLES20.*
import android.opengl.Matrix
import android.os.Bundle
import asiainnovations.com.opengles_demo.getAssetAsString
import com.opengles_demo.obj.Obj
import com.opengles_demo.obj.ObjData
import com.opengles_demo.obj.ObjReader
import com.opengles_demo.obj.ObjUtils
import java.io.InputStreamReader
import javax.microedition.khronos.opengles.GL10

class PhongFragment : MVPFragment() {
    private lateinit var teapot: Obj

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inputStream = InputStreamReader(context!!.assets.open("models/teapot.obj"))
        teapot = ObjUtils.convertToRenderable(ObjReader.read(inputStream))
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        Matrix.translateM(viewMatrix, 0, 0f, -1.5f, 3f)
    }

    override fun onEarlyDrawFrame() {
        Matrix.rotateM(modelMatrix, 0, 30 * deltaTime.toFloat() / 1000, 0.0f, 1f, 0f)
    }

    override fun onDrawFrame() {
        val vertices = ObjData.getVertices(teapot)
        shader.setVertexAttribArray("aPosition", 3, vertices)
        val normals = ObjData.getNormals(teapot)
        shader.setVertexAttribArray("aNormal", 3, normals)
        shader.setUniform3fv("objColor", floatArrayOf(1f, 1f, 1f))
        shader.setUniform3fv("lightColor", floatArrayOf(1f, 0f, 0f))
        shader.setUniformFloat("ambientStrength", 0.1f)
        shader.setUniform3fv("lightPos", floatArrayOf(3f, 3f, 3f))
        val indices = ObjData.getFaceVertexIndices(teapot)
        glDrawElements(GL_TRIANGLES, indices.capacity(), GL_UNSIGNED_INT, indices)
    }

    override fun vertexShader() = getAssetAsString(resources, "phong/vertex_shader.glsl")!!
    override fun fragmentShader() = getAssetAsString(resources, "phong/fragment_shader.glsl")!!
}