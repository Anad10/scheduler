package edu.iliauni.scheduler.API

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("Subject/GetSubjects/{id}")
    suspend fun getEvents(@Path("id") id: Int): ResponseBody

    @GET("Program/Url/{code}")
    suspend fun getProgramUrl(@Path("code") code: String): ResponseBody
}