package asiainnovations.com.opengles_demo.fragments

import android.content.pm.ActivityInfo
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment

abstract class BaseGLFragment : Fragment(), GLSurfaceView.Renderer {
    var glSurfaceView: GLSurfaceView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val frameLayout = FrameLayout(context!!)
        glSurfaceView = createGLSurfaveView()
        frameLayout.addView(glSurfaceView)
        return frameLayout
    }

    protected open fun createGLSurfaveView(): GLSurfaceView =
            GLSurfaceView(context!!).apply {
                setPreserveEGLContextOnPause(true)
                setEGLContextClientVersion(getEglContextClientVersion())
                setRenderer(this@BaseGLFragment)
                setRenderMode(getRenderMode())
            }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser)
            glSurfaceView?.requestRender()
    }

    protected open fun getEglContextClientVersion() = 2
    protected open fun getRenderMode() = GLSurfaceView.RENDERMODE_WHEN_DIRTY

    open val orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}