package com.example.checkgo.feature_admin.data.repository

import com.example.checkgo.core.data.dto.ErrorResponseDto
import com.example.checkgo.core.data.dto.UserResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import javax.inject.Inject

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
        return usersStore.stream(
            StoreReadRequest.cached(
                key = Unit,
                refresh = forceRefresh
            )
        )
    }
}