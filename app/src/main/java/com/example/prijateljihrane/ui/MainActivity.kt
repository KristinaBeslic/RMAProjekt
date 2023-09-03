package com.example.prijateljihrane.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.prijateljihrane.R
import com.example.prijateljihrane.adapters.FragmentAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var tabLayout = findViewById(R.id.tabLayout) as TabLayout
        var viewPager = findViewById(R.id.viewPager) as ViewPager

        val fragmentAdapter = FragmentAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(AddProductFragment(), "Add product")
        fragmentAdapter.addFragment(ProductsFragment(), "Products")
        fragmentAdapter.addFragment(SettingsFragment(), "Settings")

        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
}