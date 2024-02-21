package com.example.bottomnav

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.bottomnav.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var textView: TextView
    private var clickNum = 0
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        textView = findViewById<TextView>(R.id.myText)
        analytics = FirebaseAnalytics.getInstance(this)

        val upButton = findViewById<Button>(R.id.button)
        upButton.setOnClickListener{
            clickNum++
            textView.text = "You clicked {clickNum} times!"
            analytics.logEvent("button_clicked", null)
        }

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.nav_open, R.string.nav_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        binding.bottomNav.background = null
        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.bottom_home -> openFragment(HomeFragment())
                R.id.bottom_favorite -> openFragment(FavFragment())
                R.id.bottom_category -> openFragment(CategoryFragment())
                R.id.bottom_profile -> openFragment(ProfileFragment())
                R.id.bottom_setting -> openFragment(SettingFragment())
            }
            true
        }
        fragmentManager = supportFragmentManager
        openFragment(HomeFragment())
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home -> openFragment(HomeFragment())
            R.id.nav_favorite -> openFragment(FavFragment())
            R.id.nav_profile -> openFragment(ProfileFragment())
            R.id.nav_setting -> openFragment(SettingFragment())
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressedDispatcher.onBackPressed()

        }
    }

    private fun openFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}