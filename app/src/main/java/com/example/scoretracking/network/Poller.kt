package com.example.scoretracking.network

import com.example.scoretracking.model.thesportdbmodels.GameEventsModel
import com.example.scoretracking.repository.Resource
import kotlinx.coroutines.flow.Flow

interface Poller {
    fun poll(delay: Long, sportType : String): Flow<Resource<GameEventsModel>>
    fun close()
}