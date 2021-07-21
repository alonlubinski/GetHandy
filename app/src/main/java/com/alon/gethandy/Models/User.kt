package com.alon.gethandy.Models

import java.io.Serializable

data class User(
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var phone: String = "",
    var userType: String = "",
    var favorites: ArrayList<String> = ArrayList(),
    var history: ArrayList<String> = ArrayList(),
    var imageUri: String = "",

) : Serializable

