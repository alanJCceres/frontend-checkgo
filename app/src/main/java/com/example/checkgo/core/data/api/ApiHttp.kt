package com.example.checkgo.core.data.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiHttp {
    val client = HttpClient(Android) {
        expectSuccess = false
        defaultRequest {
            url("http://192.168.0.9:8080/")
            contentType(ContentType.Application.Json)
            //Aqui colocar interceptor de token
        }
        install(ContentNegotiation){
            json(Json {
                ignoreUnknownKeys=true
                prettyPrint=true
            })
        }
    }

    suspend inline fun get(endpoint: String): HttpResponse{
        return client.get { endpoint }
    }
    suspend inline fun <reified T> post(endpoint:String,data:T): HttpResponse{
        return client.post(endpoint){
            setBody(data)
        }
    }
}