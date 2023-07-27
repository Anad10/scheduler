package edu.iliauni.scheduler.API

import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.OkHttpClient
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PublicKey
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.security.spec.RSAPublicKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class Certificate {
    private val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    })

    fun initSSL(): SSLContext{
        // Create an SSLContext with the custom TrustManager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, SecureRandom())

        return sslContext
    }

    fun GetClient() : OkHttpClient{
        val sslContext = initSSL()
        // Create an OkHttpClient that trusts all certificates
        val okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .build()

        return okHttpClient
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun PostClient() : OkHttpClient{
        // Extract the RSA public key from the XML string
        val rsaPublicKeyXml = "<RSAKeyValue><Modulus>2peAb4W3rWtHm7tGbtUgVr1SV6Ioy0WOK3faPN+iPt8hoGQZL2Exqu+24Wpvhxvx//z8AqgUjKLs4iaoI6CuHVOh39shXggMi5/FXy+J5eRHEOp9+9paf0OQhykTI49ruWIARBFaQ+hO+yHBZMhjG2hQgUmBDnz6An4eMH1B360=</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>"
        val modulusBase64 = rsaPublicKeyXml.substringAfter("<Modulus>").substringBefore("</Modulus>")
        val exponentBase64 = rsaPublicKeyXml.substringAfter("<Exponent>").substringBefore("</Exponent>")

        // Convert the RSA public key to a valid PublicKey instance
        val modulusBytes = Base64.getDecoder().decode(modulusBase64)
        val exponentBytes = Base64.getDecoder().decode(exponentBase64)

        val modulus = BigInteger(1, modulusBytes)
        val exponent = BigInteger(1, exponentBytes)

        val spec = RSAPublicKeySpec(modulus, exponent)

        val keyFactory = KeyFactory.getInstance("RSA")
        val rsaPublicKey: PublicKey = keyFactory.generatePublic(spec)

        val sslContext = initSSL()

        // Create an OkHttpClient that trusts all certificates
        val okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .addInterceptor(DecryptionInterceptor(rsaPublicKey))
            .build()

        return okHttpClient
    }
}