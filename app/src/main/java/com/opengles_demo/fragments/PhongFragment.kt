package com.opengles_demo.fragments

import com.opengles_demo.GlModelRenderObject
import com.opengles_demo.GlObject
import com.opengles_demo.Quaternion
import com.opengles_demo.math.Vector3
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class PhongFragment : GlSceneFragment() {
    private inner class Teapot : GlModelRenderObject(){
        override fun obj(): String = "models/teapot.obj"
        override fun vertexShader(): String = "phong/vertex_shader.glsl"
        override fun fragmentShader(): String = "phong/fragment_shader.glsl"
        private var objColor = floatArrayOf(1f, 0f, 0f)
        private var lightColor = floatArrayOf(1f, 1f, 1f)
        private var ambientStrength = 0.1f
        private var specularStrength = 1.5f
        private var shininess = 128f

        override fun render() {
            shader.setUniform3fv("objColor", objColor)
            shader.setUniform3fv("lightColor", lightColor)
            shader.setUniform1fv("ambientStrength", ambientStrength)
            shader.setUniform3fv("lightPos", light.transform.worldPosition().asFloatArray())
            shader.setUniform3fv("eyePos", camera.transform.worldPosition().asFloatArray())
            shader.setUniform1fv("specularStrength", specularStrength)
            shader.setUniform1fv("shininess", shininess)
            super.render()
        }
    }
//    override val orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    private var light = GlObject().apply {
        transform.position = Vector3(3f, 3f, 1.5f)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        objects.add(Teapot())

        camera.transform.position = Vector3(0f,6f,-8f)
    }

    override fun update(deltaTime:Long) {
        super.update(deltaTime)
        light.transform.rotation.rotate(Quaternion.fromEular(Vector3(0f, 30f* deltaTime / 1000f * Math.PI.toFloat() / 180f,0f)))
    }
}