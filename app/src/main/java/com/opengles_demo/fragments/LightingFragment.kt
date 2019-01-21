package com.opengles_demo.fragments

import android.graphics.BitmapFactory
import android.opengl.GLES20.*
import android.opengl.Matrix
import asiainnovations.com.opengles_demo.GlShader
import asiainnovations.com.opengles_demo.getAssetAsString
import com.asiainnovations.onlyu.video.gl.GlUtil
import com.opengles_demo.R
import com.opengles_demo.gl.Cube
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class LightingFragment : MVPFragment() {
    inner class LightObject(val color: FloatArray,val rotateSpeed:Float
                            ,val xRotate: Float, val yRotate: Float, val zRotate: Float
                            ,val xTranslate: Float, val yTranslate: Float, val zTranslate: Float
    ) {
        private val lightShader: GlShader

        private val mLightPosInModelSpace = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)

        private val mLightPosInWorldSpace = FloatArray(4)

        val mLightPosInEyeSpace = FloatArray(4)

        var angleInDegrees = 0f

        protected val lightModelMatrix = FloatArray(16)
        init {
            Matrix.setIdentityM(lightModelMatrix, 0)
            lightShader = GlShader(getAssetAsString(resources, "point/vertex_shader.glsl")!!,
                    getAssetAsString(resources, "point/fragment_shader.glsl")!!)
        }
        fun draw() {
            lightShader.useProgram()
            glVertexAttrib3fv(lightShader.getAttribLocation("aPosition"), mLightPosInModelSpace,0)
            lightShader.setVertexAttribArray("aColor",  4, ByteBuffer.allocateDirect(color.size * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .apply {
                        put(color)
                        position(0)
                    }
            )
            val tempMVMatrix = FloatArray(16)
            Matrix.multiplyMM(tempMVMatrix, 0, viewMatrix, 0, lightModelMatrix, 0)
            val tempMVPMatrixM = FloatArray(16)
            Matrix.multiplyMM(tempMVPMatrixM, 0, projectionMatrix, 0, tempMVMatrix, 0)
            lightShader.setUniformMatrix4fv("uMVPMatrix", tempMVPMatrixM)

            glDrawArrays(GL_POINTS, 0, 1)
        }

        fun onEarlyDrawFrame() {
            Matrix.setIdentityM(lightModelMatrix, 0)
            angleInDegrees += rotateSpeed *deltaTime
            Matrix.rotateM(lightModelMatrix, 0, angleInDegrees, xRotate, yRotate, zRotate)
            Matrix.translateM(lightModelMatrix, 0, xTranslate, yTranslate, zTranslate)


            Matrix.multiplyMV(mLightPosInWorldSpace, 0, lightModelMatrix, 0, mLightPosInModelSpace, 0)
            Matrix.multiplyMV(mLightPosInEyeSpace, 0, viewMatrix, 0, mLightPosInWorldSpace, 0)
        }
    }

    lateinit var lights:Array<LightObject>
    var textureId: Int = 0

    private val vertexBuffer =
            ByteBuffer.allocateDirect(Cube.vertices.size * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .apply {
                        put(Cube.vertices)
                        position(0)
                    }
    private val textureMappingBuffer =
            ByteBuffer.allocateDirect(Cube.textureCoordinate.size * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .apply {
                        put(Cube.textureCoordinate)
                        position(0)
                    }

    private val normalBuffer =
            ByteBuffer.allocateDirect(Cube.cubeNormalData.size * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .apply {
                        put(Cube.cubeNormalData)
                        position(0)
                    }


    override fun onEarlyDrawFrame() {
        Matrix.rotateM(modelMatrix, 0, 30 * deltaTime.toFloat() / 1000, 0.5f, 0.5f, 0.5f)
        lights.forEach {
            it.onEarlyDrawFrame()
        }
    }

    override fun onDrawFrame() {
        shader.useProgram()
        shader.setUniformMatrix4fv("uMVPMatrix", mvpMatrix)
        shader.setUniformMatrix4fv("uMVMatrix", mvMatrix)
        shader.setVertexAttribArray("aPosition", 3, vertexBuffer)
        shader.setVertexAttribArray("aNormal", 3, normalBuffer)
        shader.setVertexAttribArray("aTextureCoordinate", 2, textureMappingBuffer)
        for(i:Int in 0..2){
            glUniform3f(shader.getUniformLocation("lights[$i].position"),lights[i].mLightPosInEyeSpace[0], lights[i].mLightPosInEyeSpace[1],lights[i].mLightPosInEyeSpace[2])
            glUniform3f(shader.getUniformLocation("lights[$i].color"),lights[i].color[0],lights[i].color[1],lights[i].color[2])
            glUniform1f(shader.getUniformLocation("lights[$i].intensive"),1f)
        }

        glDrawArrays(GL_TRIANGLES, 0, Cube.vertices.size / 3)
        lights.forEach {
            it.draw()
        }
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        eyeZ = -8f
        super.onSurfaceChanged(gl, width, height)
    }


    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        super.onSurfaceCreated(gl, config)
        textureId = GlUtil.createTextureFromBitmap(BitmapFactory.decodeResource(resources, R.mipmap.crate))

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, textureId)
        glUniform1i(shader.getUniformLocation("uTexture"), 0)
        lights = arrayOf(
                LightObject(floatArrayOf(1f,0f,0f,1f),-0.1f,0f,1f,0f,0f,0f,4f),
                LightObject(floatArrayOf(0f,1f,0f,1f),0.15f,1f,0f,0f,0f,4f,0f),
                LightObject(floatArrayOf(0f,0f,1f,1f),0.05f,0f,0f,1f,4f,0f,0f)
        )
    }

    override fun vertexShader() = getAssetAsString(resources, "lighting/vertex_shader.glsl")!!

    override fun fragmentShader() = getAssetAsString(resources, "lighting/fragment_shader.glsl")!!
}