package edu.iliauni.scheduler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import edu.iliauni.scheduler.API.ApiService
import edu.iliauni.scheduler.API.Certificate
import edu.iliauni.scheduler.Manager.RealmManager
import edu.iliauni.scheduler.objects.AppData
import edu.iliauni.scheduler.objects.UserDetail
import io.realm.kotlin.ext.query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Retrofit

class BluetoothActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        // Start a coroutine to make the network request
        CoroutineScope(Dispatchers.Main).launch {
            try{
                val responseBody = getBluetoothCodes()
                Log.d("getBluetoothCodes: ", responseBody.toString())
            } catch (exception: Exception){
                Log.e("ERROR:", exception.message.toString())
            }
        }
    }

    suspend fun getBluetoothCodes(): ResponseBody {
        val retrofit = Retrofit.Builder()
            .baseUrl(AppData.programURL)
            .client(Certificate().GetClient(AppData.idToken!!))
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        return apiService.getBluetoothCodes()
    }
}