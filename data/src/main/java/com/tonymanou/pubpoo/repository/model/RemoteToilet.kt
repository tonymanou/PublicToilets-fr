package com.tonymanou.pubpoo.repository.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class RemoteToilet(
    @SerialName("recordid")
    val id: String? = null,

    @SerialName("fields")
    val data: RemoteToiletData? = null,
)
