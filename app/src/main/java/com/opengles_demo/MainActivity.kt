package com.opengles_demo

import android.os.Bundle
import android.view.Menu
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
import com.opengles_demo.fragments.TextureMappingFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setSupportActionBar(toolbar)

        replaceFragment(TextureMappingFragment())

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(id.fragmentContainer, fragment).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            id.navigation_gles10 -> replaceFragment(GLES10Fragment())
            id.navigation_customizeshader -> replaceFragment(ShaderFragment())
            id.navigation_texture -> replaceFragment(TextureFragment())
            id.navigation_3d -> replaceFragment(CubeFragment())
            id.navigation_texture_mapping -> replaceFragment(TextureMappingFragment())
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
