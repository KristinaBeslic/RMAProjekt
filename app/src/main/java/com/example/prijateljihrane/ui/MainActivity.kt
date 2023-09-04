package com.example.prijateljihrane.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.viewpager.widget.ViewPager
import com.example.prijateljihrane.R
import com.example.prijateljihrane.adapters.FragmentAdapter
import com.google.android.material.tabs.TabLayout

const val CHANNEL_ID = "channelId"

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

        createNotificationChannel()

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Nearly expires!")
            .setContentText("You may have products that nearly expires! Check it in Products!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            if(ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, "First channel",
            NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Notification about products"

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}