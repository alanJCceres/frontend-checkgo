package com.example.checkgo.feature_admin.data.repository

import com.example.checkgo.core.data.dto.ErrorResponseDto
import com.example.checkgo.core.data.dto.UserResponseDto
import com.example.checkgo.feature_admin.data.dto.UserPatchRequestDto
import com.example.checkgo.feature_auth.data.dto.LoginUserRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.impl.extensions.get
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
    private val client: HttpClient
) {
    private val endpointBase:String = "api/v1/super_admin"

    private val usersStore: Store<Any, List<UserResponseDto>> = StoreBuilder.from(
        fetcher = Fetcher.of {
            val response = client.get("$endpointBase/users")
            if (response.status.value == 200) {
                response.body<List<UserResponseDto>>()
            } else {
                throw Exception("${response.body<ErrorResponseDto>().message}")
            }
        }
    ).build()

    fun getUsers(forceRefresh: Boolean = false): Flow<StoreReadResponse<List<UserResponseDto>>> {
        val request = if (forceRefresh) {
            StoreReadRequest.fresh(key = Unit) // Obliga a ir a la red y actualiza la caché
        } else {
            StoreReadRequest.cached(key = Unit, refresh = false) // Usa la caché en memoria
        }
        return usersStore.stream(request)
    }

    suspend fun getUserById(userPublicId: String): UserResponseDto?{
        val cached = try {
            usersStore.get(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            emptyList()
        }
        cached.firstOrNull { it.publicId == userPublicId }?.let { return it } //si existe el objeto devuelve aqui

        //si falla lo primero intenta obtenerlo de la red
        return try {
            val response = client.get("$endpointBase/user/$userPublicId")
            if (response.status.value == 200) {
                response.body<UserResponseDto>()
            } else {
                null
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }

    }

    suspend fun patchEditUser(userPublicId: String, request: UserPatchRequestDto):HttpResponse{
        return client.patch("${endpointBase}/users/${userPublicId}/credentials") {
            setBody(request)
        }
    }
}