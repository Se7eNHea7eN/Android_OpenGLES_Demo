package com.opengles_demo

import asiainnovations.com.opengles_demo.GlShader
import asiainnovations.com.opengles_demo.getAssetAsString

abstract class GlRenderObject : GlObject() {
    protected val shader: GlShader
    abstract fun vertexShader():String
    abstract fun fragmentShader():String

    init {
        shader = GlShader(
                getAssetAsString(GlApplication.context.resources, vertexShader())!!,
                getAssetAsString(GlApplication.context.resources, fragmentShader())!!
                )
    }

    override fun update() {
        super.update()
        shader.useProgram()
        shader.setUniformMatrix4fv("modelMatrix", transform.matrix())
    }

    fun setProjectionMatrix(projectionMatrix:FloatArray){
        shader.setUniformMatrix4fv("projectionMatrix", projectionMatrix)
    }

    fun setViewMatrix(viewMatrix:FloatArray){
        shader.setUniformMatrix4fv("viewMatrix", viewMatrix)
    }
    abstract fun render()
}