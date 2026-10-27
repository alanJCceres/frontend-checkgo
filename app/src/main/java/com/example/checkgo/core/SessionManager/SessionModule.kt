package com.example.checkgo.core.SessionManager

import com.example.checkgo.core.data.localStorage.AppDataStoreManager
import com.example.checkgo.core.data.localStorage.TokenManager
import com.example.checkgo.feature_admin.data.repository.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

//clase donde definimos la implementacion de sessionClearable para limpiar correctamente la sesion
@Module
@InstallIn(SingletonComponent::class)
class SessionModule {
    @Provides @IntoSet
    fun provideTokenManager(impl: TokenManager): SessionClearable = impl
    @Provides @IntoSet
    fun provideUserRepository(impl: UsersRepository): SessionClearable = impl
    @Provides @IntoSet
    fun provideAppDataStore(impl: AppDataStoreManager): SessionClearable = impl
}