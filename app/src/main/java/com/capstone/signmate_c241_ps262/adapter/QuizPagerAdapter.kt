@file:Suppress("DEPRECATION")

package com.capstone.signmate_c241_ps262.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.capstone.signmate_c241_ps262.ui.play.PlayNowAlphabetFragment
import com.capstone.signmate_c241_ps262.ui.play.PlayNowTfFragment


@Suppress("DEPRECATION")
class QuizPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragmentList = listOf(
        PlayNowAlphabetFragment(),
        PlayNowNumberFragment(),
        PlayNowTfFragment()
    )

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Alphabet"
            1 -> "Number"
            2 -> "True/False"
            else -> null
        }
    }
}
