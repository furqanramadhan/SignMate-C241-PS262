package com.capstone.signmate_c241_ps262.ui.play

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.capstone.signmate_c241_ps262.R
import com.capstone.signmate_c241_ps262.databinding.ActivityPlayNowAlphabetBinding

class PlayNowAlphabetFragment : Fragment() {
    private var _binding: ActivityPlayNowAlphabetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityPlayNowAlphabetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}