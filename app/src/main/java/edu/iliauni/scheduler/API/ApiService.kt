package edu.iliauni.scheduler.API

import edu.iliauni.scheduler.objects.Program
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("Subject/GetSubjects/{id}")
    suspend fun getEvents(@Path("id") id: Int): ResponseBody

    @GET("User/GetBluetoothCodes")
    suspend fun getBluetoothCodes(): ResponseBody

    @GET("Program/Url/{code}")
    suspend fun getProgramUrl(@Path("code") code: String): ResponseBody

    @POST("Program/Create")
    suspend fun createProgram(@Body program: Program): ResponseBody
}