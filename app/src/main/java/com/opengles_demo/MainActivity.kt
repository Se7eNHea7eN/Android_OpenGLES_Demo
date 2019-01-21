package com.opengles_demo

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import asiainnovations.com.opengles_demo.fragments.CubeFragment
import asiainnovations.com.opengles_demo.fragments.GLES10Fragment
import asiainnovations.com.opengles_demo.fragments.ShaderFragment
import asiainnovations.com.opengles_demo.fragments.TextureFragment
import com.google.android.material.navigation.NavigationView
import com.opengles_demo.R.id
import com.opengles_demo.R.layout
import com.opengles_demo.fragments.LightingFragment
import com.opengles_demo.fragments.ShaderToy.ShaderToyFragment
import com.opengles_demo.fragments.TextureMappingFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        drawer_layout.openDrawer(GravityCompat.START)

        nav_view.setNavigationItemSelectedListener(this)

        replaceFragment(TextureMappingFragment())
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun replaceFragment(fragment: Fragment) {
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
            id.shader_arts -> ShaderToyFragment.newInstance("seascape")
            else -> GLES10Fragment()
        })

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
