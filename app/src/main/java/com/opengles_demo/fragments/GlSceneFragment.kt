package com.opengles_demo.fragments

import asiainnovations.com.opengles_demo.fragments.BaseGLFragment
import com.opengles_demo.GlCamera
import com.opengles_demo.GlObject
import com.opengles_demo.GlRenderObject
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

open class GlSceneFragment : BaseGLFragment() {
    protected val camera = GlCamera()
    protected val objects  = ArrayList<GlObject>()
    private var lastDrawTime: Long = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        lastDrawTime = System.currentTimeMillis()
    }

    override fun onDrawFrame(gl: GL10?) {
        update(System.currentTimeMillis() - lastDrawTime)
        lastDrawTime = System.currentTimeMillis()
        camera.render(objects.filter { it is GlRenderObject }.map { it as GlRenderObject })
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        camera.onWindowSizeChanged(width,height)
    }

    protected open fun update(deltaTime:Long){
        objects.forEach {
            it.update()
        }
    }
}