package com.example.checkgo.feature_admin.data.repository

import com.example.checkgo.feature_admin.data.dto.CreateLocationRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArrivalLocationRepository @Inject constructor(
    private val client: HttpClient
) {
    private val endpointBase:String = "api/v1/super_admin/arrivalLocation"
    suspend fun postArrivalLocation(request: CreateLocationRequestDto):HttpResponse{
        return client.post(endpointBase) {
            setBody(request)
        }
    }
}