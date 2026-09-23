package com.example.checkgo.core.data.api

import com.example.checkgo.core.data.localStorage.TokenManager
import com.example.checkgo.feature_auth.data.dto.LoginUserResponseDto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.AttributeKey
import kotlinx.serialization.json.Json
import javax.inject.Singleton
val SkipAuth = AttributeKey<Boolean>("SkipAuth")
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(tokenManager: TokenManager): HttpClient {
        return HttpClient(Android) {
            expectSuccess = false

            defaultRequest {
                url("http://192.168.0.9:8080/")
                contentType(ContentType.Application.Json)
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val access = tokenManager.getAccessToken()
                        val refresh = tokenManager.getRefreshToken()
                        if (access != null && refresh != null) {
                            BearerTokens(access, refresh)
                        } else null
                    }

                    refreshTokens {
                        val refresh = tokenManager.getRefreshToken() ?: return@refreshTokens null

                        try {
                            val response = client.post("api/v1/auth/refresh_token") {
                                markAsRefreshTokenRequest()
                                setBody(mapOf("refreshToken" to refresh))
                            }

                            if (response.status == HttpStatusCode.OK) {
                                val newTokens = response.body<LoginUserResponseDto>()
                                tokenManager.saveTokens(
                                    newTokens.accessToken,
                                    newTokens.refreshToken,
                                    newTokens.role.toString()
                                )
                                BearerTokens(newTokens.accessToken, newTokens.refreshToken)
                            } else {
                                tokenManager.clearAll()
                                tokenManager.accessTokenExpirado()
                                null
                            }
                        } catch (e: Exception) {
                            tokenManager.clearAll()
                            tokenManager.accessTokenExpirado()
                            null
                        }
                    }

                    sendWithoutRequest { request ->
                        val path = request.url.encodedPath
                        val isPublicRoute = path.contains("login") || path.contains("refresh")
                        val shouldSkipAuth = request.attributes.contains(SkipAuth)

                        // Retorna FALSE para NO enviar token
                        // Retorna TRUE para SÍ adjuntar el Bearer Token automáticamente
                        !(isPublicRoute || shouldSkipAuth)
                    }
                }
            }
        }
    }
}