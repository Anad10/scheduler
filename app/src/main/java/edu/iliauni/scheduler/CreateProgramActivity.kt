package edu.iliauni.scheduler

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import edu.iliauni.scheduler.API.ApiService
import edu.iliauni.scheduler.API.Certificate
import edu.iliauni.scheduler.Manager.RealmManager
import edu.iliauni.scheduler.config.Constants
import edu.iliauni.scheduler.objects.Program
import edu.iliauni.scheduler.objects.UserDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreateProgramActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_program)

        val registerProgramButton = findViewById(R.id.btn_register_program) as Button
        registerProgramButton.setOnClickListener{
            val editTextProgramName: EditText = findViewById(R.id.txtProgramName)
            val programName: String = editTextProgramName.text.toString()
            val editTextServerURL: EditText = findViewById(R.id.txtServerURL)
            val serverURL: String = editTextServerURL.text.toString()

            if(programName.isEmpty()){
                editTextProgramName.error = "Program name cannot be empty"
            }
            else if(serverURL.isEmpty()){
                editTextServerURL.error = "Server URL cannot be empty"
            }
            else{
                GlobalScope.launch(Dispatchers.IO) {
                    try{
                        val program = Program()
                        program.Name = programName
                        program.Server = serverURL
                        val programUrl = createProgram(program).toString()
                        val userDetail = UserDetail().apply {
                            id = 1
                            this.programCode = programCode
                            this.programUrl = programUrl
                        }
                        RealmManager.realm.writeBlocking {
                            copyToRealm(userDetail)
                        }
                        val intent = Intent(this@CreateProgramActivity, ProgramCreationSuccessActivity::class.java)
                        startActivity(intent)
                    }
                    catch (exception: Exception){
                        Log.e("exception", exception.toString())
                    }
                }
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createProgram(program: Program): ResponseBody {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.MAIN_SERVER)
            .client(Certificate().PostClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        return apiService.createProgram(program)
    }

}