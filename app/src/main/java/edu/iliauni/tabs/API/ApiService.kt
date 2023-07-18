package edu.iliauni.tabs.API

import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.http.GET

interface ApiService {
    @GET("Subject/GetSubjects/1")
    suspend fun getEvents(): ResponseBody
}