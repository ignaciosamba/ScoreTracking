package com.example.scoretracking.network

import com.example.scoretracking.commons.snackbar.SnackBarManager
import com.example.scoretracking.commons.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.example.scoretracking.model.services.LogInterface
import com.example.scoretracking.model.thesportdbmodels.GameEventsModel
import com.example.scoretracking.repository.RemoteDataSourceTheSportDB
import com.example.scoretracking.repository.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LiveEventPoller @Inject constructor(
    private val leaguesTheSportDBRemoteDataSource: RemoteDataSourceTheSportDB,
    private val logService: LogInterface/*,
    @IoDispatcher private val dispatcher: CoroutineDispatcher*/
) : Poller{

    private val dispatcher = Dispatchers.IO
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun poll(delay: Long, sportType : String): Flow<Resource<GameEventsModel>> {
        return channelFlow {
            while (!isClosedForSend) {
                delay(delay)
                val data = leaguesTheSportDBRemoteDataSource.getLiveEventsBySport(sportType)
                send(data)
            }
        }.flowOn(dispatcher)
    }

    override fun close() {
        dispatcher.cancel()
    }

}