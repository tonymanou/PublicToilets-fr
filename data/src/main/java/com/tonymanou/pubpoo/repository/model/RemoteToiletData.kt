package com.tonymanou.pubpoo.repository.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class RemoteToiletData(
    @SerialName("arrondissement")
    val zipCode: Int? = null,

    @SerialName("adresse")
    val address: String? = null,

    @SerialName("geo_point_2d")
    val location: List<Double?>? = null,

    @SerialName("acces_pmr")
    val accessible: String? = null,

    @SerialName("horaire")
    val openingTime: String? = null,

    @SerialName("type")
    val type: String? = null,

    @SerialName("url_fiche_equipement")
    val detailsUrl: String? = null,
)
