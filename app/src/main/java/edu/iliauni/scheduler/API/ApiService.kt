package edu.iliauni.scheduler.API

import okhttp3.ResponseBody
import retrofit2.http.GET

interface ApiService {
    @GET("Subject/GetSubjects/1")
    suspend fun getEvents(): ResponseBody
}