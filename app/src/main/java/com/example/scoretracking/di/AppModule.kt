package com.example.scoretracking.di

import android.content.Context
import androidx.room.Room
import com.example.scoretracking.data.TheSportDataBase
import com.example.scoretracking.data.dao.FavoriteLeagesDAO
import com.example.scoretracking.data.dao.FavoriteTeamsDAO
import com.example.scoretracking.data.dao.LeagueDAO
import com.example.scoretracking.data.dao.TeamsDAO
import com.example.scoretracking.model.services.LogInterface
import com.example.scoretracking.network.*
import com.example.scoretracking.repository.RemoteDataSourceTheSportDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesTheSportDBApi() : TheSportDBApi {
        return Retrofit.Builder()
            .baseUrl(SPORT_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheSportDBApi::class.java)
    }

    @Provides
    @Singleton
    fun providesEspnApi() : EspnApi {
        return Retrofit.Builder()
            .baseUrl(ESPN_SPORT_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EspnApi::class.java)
    }

    @Singleton
    @Provides
    fun providesAppDataBase(@ApplicationContext context: Context) : TheSportDataBase =
        Room.databaseBuilder(
            context,
            TheSportDataBase::class.java,
            "sportstracker_database")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideLeagueDao(theSportDataBase: TheSportDataBase) : LeagueDAO =
        theSportDataBase.leaguesDAO()

    @Singleton
    @Provides
    fun provideFavoriteLeagueDao(theSportDataBase: TheSportDataBase) : FavoriteLeagesDAO =
        theSportDataBase.favoriteLeaguesDao()

    @Singleton
    @Provides
    fun provideTeamDao(theSportDataBase: TheSportDataBase) : TeamsDAO =
        theSportDataBase.teamsDao()

    @Singleton
    @Provides
    fun provideFavoriteTeamDao(theSportDataBase: TheSportDataBase) : FavoriteTeamsDAO =
        theSportDataBase.favoriteTeamsDao()
}