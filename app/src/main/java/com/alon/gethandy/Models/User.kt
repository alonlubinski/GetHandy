package com.alon.gethandy.Models

import java.io.Serializable

data class User(
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var phone: String = "",
    var userType: String = "",
    var lon: Double = 0.0,
    var lat: Double = 0.0,
    var imageUri: String = "",

) : Serializable

