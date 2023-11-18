package edu.iliauni.scheduler.API

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class RequestLoggingInterceptor : Interceptor {
    private val TAG = "RequestLoginInterceptor"
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        // Log request details including method, URL, and headers
        Log.d(TAG, "Request: ${request.method()} ${request.url()}")
        val headers = request.headers()
        for (i in 0 until headers.size()) {
            Log.d(TAG,"${headers.name(i)}: ${headers.value(i)}")
        }
        return chain.proceed(request)
    }
}