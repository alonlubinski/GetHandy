package com.alon.gethandy.Models

import java.io.Serializable

data class Business(
    val ownerEmail: String = "",
    var businessName: String = "",
    var businessAddress: String = "",
    var businessImage: String = "",
    var totalRating: Int = 0,
    var numOfRates: Int = 0,
    var distance: Double = 0.0,
    var startHours: String = "",
    var endHours: String = "",
    var phoneNumber: String = "",
    var description: String = "",
    var ownerName: String = ""
) : Serializable

