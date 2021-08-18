package com.tonymanou.pubpoo.contract

import com.tonymanou.pubpoo.model.Toilet

interface ToiletListApiContract {
    suspend fun fetch(accessibleOnly: Boolean, offset: Int, limit: Int): List<Toilet>
}
