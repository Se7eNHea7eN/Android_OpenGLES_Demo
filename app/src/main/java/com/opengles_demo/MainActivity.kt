package com.opengles_demo

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import asiainnovations.com.opengles_demo.fragments.*
import com.google.android.material.navigation.NavigationView
import com.gyf.barlibrary.BarHide
import com.gyf.barlibrary.ImmersionBar
import com.opengles_demo.R.id
import com.opengles_demo.fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var initFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ImmersionBar.with(this)
                .transparentBar()  //透明状态栏，
                .barColor(android.R.color.transparent)
                .barAlpha(1f)  //状态栏透明度，不写默认0.0f
                .hideBar(BarHide.FLAG_HIDE_BAR)  //隐藏状态栏或导航栏或两者，不写默认不隐藏
                .init()
        dummy.setOnClickListener {
            if(!drawer_layout.isDrawerOpen(nav_view))
                drawer_layout.openDrawer(GravityCompat.START)
        }

        drawer_layout.openDrawer(GravityCompat.START)

        nav_view.setNavigationItemSelectedListener(this)
        if(!initFlag){
            initFlag = true
            replaceFragment(ShaderToyFragment.newInstance("heartbeat"))
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // 如果你的app可以横竖屏切换，并且适配4.4或者emui3手机请务必在onConfigurationChanged方法里添加这句话
        ImmersionBar.with(this).init()
    }
    override fun onDestroy() {
        super.onDestroy()
        ImmersionBar.with(this).destroy()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun replaceFragment(fragment: BaseGLFragment) {
        supportFragmentManager.beginTransaction().replace(id.fragmentContainer, fragment).commit()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        replaceFragment(when (item.itemId) {
            id.navigation_gles10 -> GLES10Fragment()
            id.navigation_customizeshader -> ShaderFragment()
            id.navigation_texture -> TextureFragment()
            id.navigation_3d -> CubeFragment()
            id.navigation_texture_mapping -> TextureMappingFragment()
            id.navigation_lighting -> LightingFragment()
            id.gradient -> ShaderToyFragment.newInstance("gradient")
            id.gradient2 -> ShaderToyFragment.newInstance("gradient2")
            id.seascape -> ShaderToyFragment.newInstance("seascape")
            id.heartbeat -> ShaderToyFragment.newInstance("heartbeat")
            id.hexagone -> ShaderToyFragment.newInstance("hexagone")
            id.cloud2d -> ShaderToyFragment.newInstance("2d_cloud")
            id.mandelbrot -> ShaderToyFragment.newInstance("mandelbrot")
            id.goo -> ShaderToyFragment.newInstance("goo")
            id.cloud -> ShaderToyFragment.newInstance("cloudy")
            id.edgeDetect -> CameraFilterFragment.newInstance("edge_detect")
            id.pixelize -> CameraFilterFragment.newInstance("pixelize")
            id.waterColor -> CameraFilterFragment.newInstance("water_color")
            id.aberration -> CameraFilterFragment.newInstance("chromatic_aberration")
            id.relief -> ConvolutionFragment.newInstance(KernalProvider.relief(),3,0.5f)
            id.blur -> 11.let {
                ConvolutionFragment.newInstance(KernalProvider.gauss(it,9f),it)
            }
            else -> GLES10Fragment()
        }.apply {
            requestedOrientation = orientation
        })

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
