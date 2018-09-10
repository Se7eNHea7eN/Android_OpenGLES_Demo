package com.opengles_demo

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import asiainnovations.com.opengles_demo.fragments.CubeFragment
import asiainnovations.com.opengles_demo.fragments.GLES10Fragment
import asiainnovations.com.opengles_demo.fragments.ShaderFragment
import asiainnovations.com.opengles_demo.fragments.TextureFragment
import com.opengles_demo.R.id
import com.opengles_demo.R.layout
import com.opengles_demo.fragments.TextureMappingFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                id.navigation_gles10 -> replaceFragment(GLES10Fragment())
                id.navigation_customizeshader -> replaceFragment(ShaderFragment())
                id.navigation_texture -> replaceFragment(TextureFragment())
                id.navigation_3d -> replaceFragment(CubeFragment())
                id.navigation_texture_mapping -> replaceFragment(TextureMappingFragment())
            }
            true
        }
        navigation.selectedItemId = id.navigation_gles10
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(id.fragmentContainer, fragment).commit()
    }
}
