package com.tonymanou.pubpoo.repository.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class RemoteToilet(
    @SerialName("recordid")
    val id: String? = null,

    @SerialName("fields")
    val data: RemoteToiletData? = null,
)
