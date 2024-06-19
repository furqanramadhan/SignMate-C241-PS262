package com.capstone.signmate_c241_ps262.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.signmate_c241_ps262.R

class ImageViewModel : ViewModel() {
    private val imageResources = listOf(
        R.drawable.ic_angka_1,
        R.drawable.ic_angka_2,
        R.drawable.ic_angka_3,
        R.drawable.ic_angka_4,
        R.drawable.ic_angka_5,
        R.drawable.ic_angka_6,
        R.drawable.ic_angka_7,
        R.drawable.ic_angka_8,
        R.drawable.ic_angka_9,
        R.drawable.ic_angka_10,
        R.drawable.ic_huruf_a,
        R.drawable.ic_huruf_b,
        R.drawable.ic_huruf_c,
        R.drawable.ic_huruf_d,
        R.drawable.ic_huruf_e,
        R.drawable.ic_huruf_f,
        R.drawable.ic_huruf_g,
        R.drawable.ic_huruf_h,
        R.drawable.ic_huruf_i,
        R.drawable.ic_huruf_j,
        R.drawable.ic_huruf_k,
        R.drawable.ic_huruf_l,
        R.drawable.ic_huruf_m,
        R.drawable.ic_huruf_n,
        R.drawable.ic_huruf_o,
        R.drawable.ic_huruf_p,
        R.drawable.ic_huruf_q,
        R.drawable.ic_huruf_r,
        R.drawable.ic_huruf_s,
        R.drawable.ic_huruf_t,
        R.drawable.ic_huruf_u,
        R.drawable.ic_huruf_v,
        R.drawable.ic_huruf_w,
        R.drawable.ic_huruf_x,
        R.drawable.ic_huruf_y,
        R.drawable.ic_huruf_z
    )

    private val _currentIndex = MutableLiveData<Int>().apply { value = 0 }
    val currentIndex: LiveData<Int> = _currentIndex

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    fun getCurrentImageResource(): Int {
        val index = _currentIndex.value ?: 0
        return imageResources[index]
    }

    fun moveToPreviousImage() {
        val currentIndexValue = _currentIndex.value ?: 0
        if (currentIndexValue > 0) {
            _currentIndex.value = currentIndexValue - 1
        } else {
            _toastMessage.value = "Cannot go previous back"
        }
    }
    fun moveToNextImage() {
        val currentIndex = _currentIndex.value ?: 0
        val newIndex = (currentIndex + 1) % imageResources.size
        _currentIndex.value = newIndex
    }
}
