package com.alon.gethandy.Models

import java.io.Serializable

data class Business(
    val businessId: String = "",
    var businessName: String = "",
    var businessAddress: String = "",
    var businessImage: String = "",
    var totalRating: Double = 0.0,
    var numOfRates: Int = 0,
    var distance: Double = 0.0
) : Serializable
