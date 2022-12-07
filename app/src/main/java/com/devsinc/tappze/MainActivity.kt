package com.devsinc.tappze

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.devsinc.tappze.data.utils.Constants
import com.devsinc.tappze.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Firebase.database.setPersistenceEnabled(true)

        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.profileFragment)
                    true
                }
                R.id.nav_nfc -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.nfcFragment)
                    true
                }
                R.id.nav_share -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.shareFragment)
                    true
                }
                R.id.nav_analytics -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.analyticsFragment)
                    true
                }
                R.id.nav_settings -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser != null) {
            binding.bottomNavView.visibility = View.VISIBLE
            // navigate to profile fragment if on welcome fragment
            if (findNavController(R.id.nav_host_fragment).currentDestination?.id == R.id.welcomeFragment) {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_welcomeFragment_to_profileFragment)
            }
            if (Constants.userLoggedIn == null) {

            }
        }
    }
}