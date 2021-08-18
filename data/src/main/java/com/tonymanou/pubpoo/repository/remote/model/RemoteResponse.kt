package com.tonymanou.pubpoo.repository.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class RemoteResponse(
    @SerialName("records")
    val list: List<RemoteToilet> = emptyList(),
)
