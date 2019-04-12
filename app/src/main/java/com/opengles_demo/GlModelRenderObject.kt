package com.opengles_demo

import android.opengl.GLES20
import com.opengles_demo.GlApplication.Companion.context
import com.opengles_demo.obj.Obj
import com.opengles_demo.obj.ObjData
import com.opengles_demo.obj.ObjReader
import com.opengles_demo.obj.ObjUtils
import java.io.InputStreamReader
import java.nio.FloatBuffer
import java.nio.IntBuffer

abstract class GlModelRenderObject : GlRenderObject() {
    abstract fun obj():String

    private var obj: Obj
    private var vertices: FloatBuffer
    private var normals: FloatBuffer
    private var indices: IntBuffer

    init{
        val inputStream = InputStreamReader(context!!.assets.open(obj()))
        obj = ObjUtils.convertToRenderable(ObjReader.read(inputStream))
        vertices = ObjData.getVertices(obj)
        normals = ObjData.getNormals(obj)
        indices = ObjData.getFaceVertexIndices(obj)

        shader.useProgram()
        shader.setVertexAttribArray("aPosition", 3, vertices)
        shader.setVertexAttribArray("aNormal", 3, normals)
    }

    override fun render() {
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.capacity(), GLES20.GL_UNSIGNED_INT, indices)
    }
}