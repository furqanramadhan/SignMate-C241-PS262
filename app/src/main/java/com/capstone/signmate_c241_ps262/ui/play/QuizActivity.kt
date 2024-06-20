package com.capstone.signmate_c241_ps262.ui.play

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import com.capstone.signmate_c241_ps262.R
import com.capstone.signmate_c241_ps262.adapter.QuizPagerAdapter
import com.capstone.signmate_c241_ps262.databinding.ActivityQuizBinding
import com.google.android.material.tabs.TabLayout

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#256656")

        val viewPager: ViewPager = binding.viewPager
        val tabLayout: TabLayout = binding.tabs

        val adapter = QuizPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }
}


