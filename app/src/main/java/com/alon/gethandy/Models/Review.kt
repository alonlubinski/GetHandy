package com.alon.gethandy.Models

data class Review(
    val reviewId: String,
    val reviewContent: String,
    val reviewRating: Int,
    val reviewDate: String,
    val reviewBusinessId: String,
    val reviewUserId: String,
    val reviewUserName: String,
    //val reviewUserImageUri: String
)
