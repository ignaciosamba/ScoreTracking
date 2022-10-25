package com.example.scoretracking.di

import com.example.scoretracking.model.service.*
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
    abstract fun provideStorageService(impl: StorageFavoriteTeamsImpl): StorageFavoriteTeamsInterface
}