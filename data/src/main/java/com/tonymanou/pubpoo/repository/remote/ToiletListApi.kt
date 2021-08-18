package com.tonymanou.pubpoo.repository.remote

import com.tonymanou.pubpoo.contract.ToiletListApiContract
import com.tonymanou.pubpoo.mapper.TOILET_ACCESSIBLE_VALUE_TRUE
import com.tonymanou.pubpoo.mapper.toModel
import com.tonymanou.pubpoo.model.Toilet
import com.tonymanou.pubpoo.repository.remote.model.RemoteResponse
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json as KotlinxSerializationJson

class ToiletListApi : ToiletListApiContract {

    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                json = KotlinxSerializationJson {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    override suspend fun fetch(
        accessibleOnly: Boolean,
        offset: Int,
        limit: Int,
    ): List<Toilet> {
        val response = client.get<RemoteResponse>("https://data.ratp.fr/api/records/1.0/search/") {
            parameter("dataset", "sanisettesparis2011")
            parameter("start", offset)
            parameter("rows", limit)
            if (accessibleOnly) {
                parameter("refine.acces_pmr", TOILET_ACCESSIBLE_VALUE_TRUE)
            }
        }

        return response.toModel()
    }

}
