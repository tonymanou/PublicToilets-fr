package com.tonymanou.pubpoo.mapper

import com.tonymanou.pubpoo.model.Location
import com.tonymanou.pubpoo.repository.model.RemoteResponse
import com.tonymanou.pubpoo.repository.model.RemoteToilet
import com.tonymanou.pubpoo.repository.model.RemoteToiletData
import org.junit.Assert.*
import org.junit.Test

class ToiletMapperTest {

    companion object {
        private const val DEFAULT_ID = "12345"
        private const val DEFAULT_ZIP_CODE = 42
        private const val DEFAULT_ADDRESS = "address"
        private const val DEFAULT_LOCATION_LATITUDE = 1.0
        private const val DEFAULT_LOCATION_LONGITUDE = 2.0
        private val DEFAULT_LOCATION = listOf(DEFAULT_LOCATION_LATITUDE, DEFAULT_LOCATION_LONGITUDE)
        private const val DEFAULT_ACCESSIBLE = "Oui"
        private const val DEFAULT_OPENING_TIME = "unknown"
        private const val DEFAULT_TYPE = "TOILETTES"
        private const val DEFAULT_DETAILS_URL = "http://urlzs.com/qGfk"
    }

    @Test
    fun `test toilet mapper empty`() {
        // Given
        val data = RemoteResponse()

        // When
        val result = data.toModel()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `test toilet mapper valid toilet`() {
        // Given
        val data = RemoteResponse(
            list = listOf(
                createToilet(),
            )
        )

        // When
        val result = data.toModel()

        // Then
        assertEquals(1, result.size)
        val toilet = result[0]
        assertEquals("$DEFAULT_ZIP_CODE $DEFAULT_ADDRESS", toilet.address)
        assertEquals(
            Location(
                latitude = DEFAULT_LOCATION_LATITUDE,
                longitude = DEFAULT_LOCATION_LONGITUDE,
            ),
            toilet.location
        )
        assertEquals(DEFAULT_OPENING_TIME, toilet.openingHour)
        assertTrue(toilet.accessible)
        assertEquals(DEFAULT_DETAILS_URL, toilet.detailsUrl)
    }

    @Test
    fun `test toilet mapper valid toilet missing values`() {
        // Given
        val data = RemoteResponse(
            list = listOf(
                createToilet(
                    zipCode = null
                ),
                createToilet(
                    address = null
                ),
                createToilet(
                    accessible = null
                ),
                createToilet(
                    openingTime = null
                ),
                createToilet(
                    detailsUrl = null
                ),
            )
        )

        // When
        val result = data.toModel()

        // Then
        assertEquals(data.list.size, result.size)
        result[0].also { toilet ->
            assertEquals(DEFAULT_ADDRESS, toilet.address)
            assertEquals(
                Location(
                    latitude = DEFAULT_LOCATION_LATITUDE,
                    longitude = DEFAULT_LOCATION_LONGITUDE,
                ),
                toilet.location
            )
            assertEquals(DEFAULT_OPENING_TIME, toilet.openingHour)
            assertTrue(toilet.accessible)
            assertEquals(DEFAULT_DETAILS_URL, toilet.detailsUrl)
        }
        result[1].also { toilet ->
            assertEquals("$DEFAULT_ZIP_CODE", toilet.address)
            assertEquals(
                Location(
                    latitude = DEFAULT_LOCATION_LATITUDE,
                    longitude = DEFAULT_LOCATION_LONGITUDE,
                ),
                toilet.location
            )
            assertEquals(DEFAULT_OPENING_TIME, toilet.openingHour)
            assertTrue(toilet.accessible)
            assertEquals(DEFAULT_DETAILS_URL, toilet.detailsUrl)
        }
        result[2].also { toilet ->
            assertEquals("$DEFAULT_ZIP_CODE $DEFAULT_ADDRESS", toilet.address)
            assertEquals(
                Location(
                    latitude = DEFAULT_LOCATION_LATITUDE,
                    longitude = DEFAULT_LOCATION_LONGITUDE,
                ),
                toilet.location
            )
            assertEquals(DEFAULT_OPENING_TIME, toilet.openingHour)
            assertFalse(toilet.accessible)
            assertEquals(DEFAULT_DETAILS_URL, toilet.detailsUrl)
        }
        result[3].also { toilet ->
            assertEquals("$DEFAULT_ZIP_CODE $DEFAULT_ADDRESS", toilet.address)
            assertEquals(
                Location(
                    latitude = DEFAULT_LOCATION_LATITUDE,
                    longitude = DEFAULT_LOCATION_LONGITUDE,
                ),
                toilet.location
            )
            assertNull(toilet.openingHour)
            assertTrue(toilet.accessible)
            assertEquals(DEFAULT_DETAILS_URL, toilet.detailsUrl)
        }
        result[4].also { toilet ->
            assertEquals("$DEFAULT_ZIP_CODE $DEFAULT_ADDRESS", toilet.address)
            assertEquals(
                Location(
                    latitude = DEFAULT_LOCATION_LATITUDE,
                    longitude = DEFAULT_LOCATION_LONGITUDE,
                ),
                toilet.location
            )
            assertEquals(DEFAULT_OPENING_TIME, toilet.openingHour)
            assertTrue(toilet.accessible)
            assertNull(toilet.detailsUrl)
        }
    }

    @Test
    fun `test toilet mapper invalid toilet missing value`() {
        // Given
        val data = RemoteResponse(
            list = listOf(
                createToilet(
                    id = null
                ),
                RemoteToilet(
                    id = DEFAULT_ID,
                    data = null,
                ),
                createToilet(
                    location = null
                ),
                createToilet(
                    zipCode = null,
                    address = null,
                ),
                createToilet(
                    type = null,
                ),
                createToilet(
                    type = "unknown",
                ),
            )
        )

        // When
        val result = data.toModel()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `test location mapper valid value`() {
        // Given
        listOf(
            listOf(DEFAULT_LOCATION_LATITUDE, DEFAULT_LOCATION_LONGITUDE),
            listOf(DEFAULT_LOCATION_LATITUDE, DEFAULT_LOCATION_LONGITUDE, null),
            listOf(DEFAULT_LOCATION_LATITUDE, DEFAULT_LOCATION_LONGITUDE, 1.2),
        ).forEach { list ->
            // When
            val result = list.toLocation()

            // Then
            assertEquals(
                Location(
                    latitude = DEFAULT_LOCATION_LATITUDE,
                    longitude = DEFAULT_LOCATION_LONGITUDE,
                ),
                result
            )
        }
    }

    @Test
    fun `test location mapper invalid value`() {
        // Given
        listOf(
            listOf(),
            listOf(null),
            listOf(null, null),
            listOf(null, null, null),
            listOf(DEFAULT_LOCATION_LATITUDE),
            listOf(DEFAULT_LOCATION_LATITUDE, null),
            listOf(DEFAULT_LOCATION_LATITUDE, null, DEFAULT_LOCATION_LONGITUDE),
            listOf(null, DEFAULT_LOCATION_LONGITUDE),
            listOf(null, DEFAULT_LOCATION_LONGITUDE, DEFAULT_LOCATION_LONGITUDE),
        ).forEach { list ->
            // When
            val result = list.toLocation()

            // Then
            assertNull(result)
        }
    }

    private fun createToilet(
        id: String? = DEFAULT_ID,
        zipCode: Int? = DEFAULT_ZIP_CODE,
        address: String? = DEFAULT_ADDRESS,
        location: List<Double?>? = DEFAULT_LOCATION,
        accessible: String? = DEFAULT_ACCESSIBLE,
        openingTime: String? = DEFAULT_OPENING_TIME,
        type: String? = DEFAULT_TYPE,
        detailsUrl: String? = DEFAULT_DETAILS_URL,
    ) = RemoteToilet(
        id = id,
        data = RemoteToiletData(
            zipCode = zipCode,
            address = address,
            location = location,
            accessible = accessible,
            openingTime = openingTime,
            type = type,
            detailsUrl = detailsUrl,
        ),
    )

}
