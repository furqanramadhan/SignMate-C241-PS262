package com.capstone.signmate_c241_ps262.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    val id: String = "",
    val email: String = "",
    val gender: String = "",
    val photo: String = "",
    val phoneNumber: String = "",
    val birthDate: String = "",
    val name: String = ""
) : Parcelable {
    constructor() : this("", "", "", "", "", "", "")
}