package com.capstone.signmate_c241_ps262.ui.dictionary

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.capstone.signmate_c241_ps262.R
import com.capstone.signmate_c241_ps262.databinding.ActivityDictionaryBinding
import com.capstone.signmate_c241_ps262.viewmodel.ImageViewModel

class DictionaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDictionaryBinding
    private lateinit var viewModel: ImageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.parseColor("#256656")

        // Inisialisasi binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dictionary)
        binding.lifecycleOwner = this  // untuk LiveData

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this)[ImageViewModel::class.java]
        binding.viewModel = viewModel

        // Observing the currentIndex to update the ImageView
        viewModel.currentIndex.observe(this) {
            loadCurrentImage()
        }

        // Observe the toastMessage to show a Toast
        viewModel.toastMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        // Button click listeners
        binding.btnPrevious.setOnClickListener {
            viewModel.moveToPreviousImage()
        }

        binding.btnNext.setOnClickListener {
            viewModel.moveToNextImage()
        }
    }
    private fun loadCurrentImage() {
        Glide.with(this)
            .load(viewModel.getCurrentImageResource())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.ivDictionary)
    }
}