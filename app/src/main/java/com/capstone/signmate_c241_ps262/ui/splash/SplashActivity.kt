package com.capstone.signmate_c241_ps262.ui.splash

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.signmate_c241_ps262.databinding.ActivitySplashBinding
import com.capstone.signmate_c241_ps262.ui.opening.OpeningActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.parseColor("#256656")


        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val pref = SettingPreferences.getInstance(application.dataStore)
//        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref))[SettingViewModel::class.java]
//        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
//            if (isDarkModeActive) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//        }

        window.decorView.postDelayed({
            startActivity(Intent(this, OpeningActivity::class.java))
            finish()
        }, 3000)
    }
}