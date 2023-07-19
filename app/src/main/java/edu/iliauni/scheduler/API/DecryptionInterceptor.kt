package edu.iliauni.scheduler.API

import java.security.PublicKey

class DecryptionInterceptor(private val rsaPublicKey: PublicKey) : okhttp3.Interceptor {
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()

        // Add the "Encrypted:true" header to the request
        val encryptedRequest = originalRequest.newBuilder()
            .header("Encrypted", "true")
            .build()

        val response = chain.proceed(encryptedRequest)

        // Decrypt the response here using the rsaPublicKey
        // For example, you can read the response body as a byte array and then decrypt it with the RSA public key

        return response
    }
}