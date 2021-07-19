package com.alon.gethandy.Models

data class User(
    var firstName: String,
    var lastName: String,
    var email: String,
    var userType: String,
    var favorites: ArrayList<String>,
    var history: ArrayList<String>
)

