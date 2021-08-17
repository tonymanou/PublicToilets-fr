package com.tonymanou.pubpoo.model

data class Toilet(
    val id: String,
    val address: String,
    val location: Location,
    val openingHour: String?,
    val accessible: Boolean,
    val detailsUrl: String?,
)
