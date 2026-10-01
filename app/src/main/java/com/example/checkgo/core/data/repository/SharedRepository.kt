package com.example.checkgo.core.data.repository

import com.example.checkgo.core.data.dto.DeviceRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class SharedRepository @Inject constructor(
    private val client: HttpClient
) {
    private val endpointBase:String = "api/v1/shared"
    suspend fun putAndroidId(request: DeviceRequestDto):HttpResponse{
        return client.put("${endpointBase}/device") {
            setBody(request)
        }
    }
}