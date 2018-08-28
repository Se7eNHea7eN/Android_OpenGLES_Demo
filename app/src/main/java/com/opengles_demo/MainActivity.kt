package com.opengles_demo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import asiainnovations.com.opengles_demo.fragments.CustomizShaderFragment
import asiainnovations.com.opengles_demo.fragments.GLES10Fragment
import asiainnovations.com.opengles_demo.fragments.TextureFragment
import asiainnovations.com.opengles_demo.fragments.ThreeDFragment
import com.opengles_demo.R.id
import com.opengles_demo.R.layout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                id.navigation_gles10 -> replaceFragment(GLES10Fragment())
                id.navigation_customizeshader -> replaceFragment(CustomizShaderFragment())
                id.navigation_texture -> replaceFragment(TextureFragment())
                id.navigation_3d -> replaceFragment(ThreeDFragment())
            }
            true
        }
        navigation.selectedItemId = id.navigation_gles10
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(id.fragmentContainer, fragment).commit()
    }
}
