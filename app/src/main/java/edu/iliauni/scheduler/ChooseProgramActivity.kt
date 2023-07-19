package edu.iliauni.scheduler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import edu.iliauni.scheduler.API.ApiService
import edu.iliauni.scheduler.API.Certificate
import edu.iliauni.scheduler.Manager.RealmManager
import edu.iliauni.scheduler.objects.UserDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Retrofit

class ChooseProgramActivity : AppCompatActivity() {
    companion object{
        private const val BASE_URL = "https://134.122.90.22:7137/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_program)

        val loginButton = findViewById(R.id.btn_login) as Button
        loginButton.setOnClickListener{
            val editTextProgramCode: EditText = findViewById(R.id.txtProgramCode)
            val programCode: String = editTextProgramCode.text.toString()

            if (programCode.isEmpty()) {
                editTextProgramCode.error = "Program code cannot be empty"
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    try{
                        val programUrl = getProgramUrl(programCode).toString()
                        val userDetail = UserDetail().apply {
                            id = 1
                            this.programCode = programCode
                            this.programUrl = programUrl
                        }
                        RealmManager.realm.writeBlocking {
                            copyToRealm(userDetail)
                        }
                        val intent = Intent(this@ChooseProgramActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    catch (exception: Exception){
                        Log.e("exception",exception.toString())
                    }
                }
            }
        }

        val createProgramButton = findViewById(R.id.btn_create_program) as Button
        createProgramButton.setOnClickListener{
            val intent = Intent(this, CreateProgramActivity::class.java)
            startActivity(intent)
        }
    }

    suspend fun getProgramUrl(code: String): ResponseBody {
       val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(Certificate().GetClient())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        return apiService.getProgramUrl(code)

    }
}