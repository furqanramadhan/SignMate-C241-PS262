package com.capstone.signmate_c241_ps262.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var gender: String = "",
    var phoneNumber: String = "",
    var birthDate: String = "",
    var photo: String = ""
) : Parcelable {
    constructor() : this("", "", "", "", "", "", "")
}