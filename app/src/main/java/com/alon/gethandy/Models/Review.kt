package com.alon.gethandy.Models

import java.io.Serializable

data class Review(
    val reviewId: String = "",
    val reviewContent: String = "",
    val reviewRating: Int = 1,
    val reviewDate: String = "",
    val reviewBusinessId: String = "",
    val reviewUserId: String = "",
    val reviewUserName: String = "",
    val reviewUserImageUri: String = ""
) : Serializable
