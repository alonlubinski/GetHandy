package com.alon.gethandy.Models

data class Business(
    val businessId: String,
    var businessName: String,
    var businessAddress: String,
    var businessImage: String,
    var totalRating: Double,
    var numOfRates: Int,
    var distance: Double
)
