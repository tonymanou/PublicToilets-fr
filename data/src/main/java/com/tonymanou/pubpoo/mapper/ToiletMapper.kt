package com.tonymanou.pubpoo.mapper

import androidx.annotation.VisibleForTesting
import com.tonymanou.pubpoo.model.Location
import com.tonymanou.pubpoo.model.Toilet
import com.tonymanou.pubpoo.repository.model.RemoteResponse

internal const val TOILET_ACCESSIBLE_VALUE_TRUE = "Oui"
private const val TOILET_TYPE = "TOILETTES"

internal fun RemoteResponse.toModel(): List<Toilet> {
    return list.mapNotNull { remote ->
        val id = remote.id ?: return@mapNotNull null
        val data = remote.data ?: return@mapNotNull null
        if (data.type != TOILET_TYPE) {
            return@mapNotNull null
        }

        val zip = data.zipCode
        val address = data.address?.takeUnless { it.isEmpty() }
        val fullAddress = when {
            zip != null && address != null -> "$zip $address"
            zip != null -> zip.toString()
            address != null -> address
            else -> return@mapNotNull null
        }

        val location = data.location?.toLocation() ?: return@mapNotNull null

        Toilet(
            id = id,
            address = fullAddress,
            location = location,
            openingHour = data.openingTime,
            accessible = data.accessible == TOILET_ACCESSIBLE_VALUE_TRUE,
            detailsUrl = data.detailsUrl,
        )
    }
}

@VisibleForTesting
internal fun List<Double?>.toLocation(): Location? {
    if (size < 2) {
        return null
    }
    return Location(
        latitude = this[0] ?: return null,
        longitude = this[1] ?: return null,
    )
}
