package com.example.checkgo.feature_auth.data.store

import com.example.checkgo.feature_auth.data.repository.AuthRepository

object StoreProviders{
    private val repository = AuthRepository()
    // El key es un String (el userId) y retorna el User del dominio
//    val userProfileStore: Store<String, User> = StoreBuilder.from(
//        fetcher = Fetcher.of { userId: String ->
//            // Llama al repo y mapea a dominio
//            repository.getUser(userId).toDomain()
//        }
//    ).build()
}