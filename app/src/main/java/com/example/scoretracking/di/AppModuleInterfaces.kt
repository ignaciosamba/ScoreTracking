package com.example.scoretracking.di

import com.example.scoretracking.model.services.*
import com.example.scoretracking.network.LiveEventPoller
import com.example.scoretracking.network.Poller
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModuleInterfaces {

    @Binds
    abstract fun provideAccountService(impl: AccountIml): AccountInterface

    @Binds
    abstract fun provideLogService(impl: LogImpl): LogInterface

    @Binds
    abstract fun provideStorageTeamService(impl: StorageFavoriteImpl): StorageFavoriteTeamsInterface

    @Binds
    abstract fun provideStorageLeagueService(impl: StorageFavoriteImpl): StorageLeagueInterface
}