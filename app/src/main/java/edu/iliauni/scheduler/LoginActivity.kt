package edu.iliauni.scheduler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import edu.iliauni.scheduler.API.ApiService
import edu.iliauni.scheduler.API.Certificate
import edu.iliauni.scheduler.Manager.RealmManager
import edu.iliauni.scheduler.Utils.AlertDialogHelper
import edu.iliauni.scheduler.Utils.GoogleAuthUtility
import edu.iliauni.scheduler.config.Constants
import edu.iliauni.scheduler.objects.*
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Retrofit

class LoginActivity : AppCompatActivity() {
    private var TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val googleSignInClient = GoogleAuthUtility.initialize(this)

        val signInButton = findViewById<Button>(R.id.btn_signin)
        signInButton.setOnClickListener {
            if (googleSignInClient != null) {
                GoogleAuthUtility.signIn(this, googleSignInClient!!)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)

            // Signed in successfully, get user details
            //val displayName = account?.displayName
            //val email = account?.email
            val idToken = account?.idToken

            // Use the user details as needed
            Log.d(TAG, "Signed in successfully!")
            Log.d(TAG, "ID Token: $idToken")

            // Proceed with sending the ID token to your backend
            val userId = getUserID(idToken)
            RealmManager.realm.writeBlocking {
                // get UserDetails from Realm
                val userDetail = this.query<UserDetail>().first()
                //userDetail.find()?.userName = email.toString()
                userDetail.find()?.userId = userId
                userDetail.find()?.idToken = idToken!!
            }
            AppData.idToken = idToken;
            GlobalScope.launch(Dispatchers.IO) {
                try{
                    val responseString = getEvents(userId).string()
                    createDB(responseString)
                }
                catch (exception: Exception){
                    Log.e(TAG, exception.toString())
                    AlertDialogHelper.showAlert(this@LoginActivity, "Error", exception.message.toString())
                }
                finally {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        } catch (e: ApiException) {
            // Sign in failed, handle the error
            println("Sign in failed: ${e.message}")
        }
    }

    private fun getUserID(idToken: String?): Int{
        // Perform API request to send the ID token
        return 1;
        TODO()
    }

    fun createDB(responseString: String){
        val response = JSONArray(responseString)
        for (i in 0 until response.length()) {
            val jsonObject = response.getJSONObject(i)
            val event = Event().apply {
                id = jsonObject.getString("id").toLong()
                name = jsonObject.getString("name")
                description = jsonObject.getString("description")
                val events = jsonObject.getJSONArray("events")
                for (j in 0 until events.length()) {
                    val event = events.getJSONObject(j)
                    startDates?.add(event.getString("startDate"))
                    endDates?.add(event.getString("endDate"))
                }

                val users = events.getJSONObject(0).getJSONArray("attendances")
                val participants = users.getJSONObject(0).getJSONArray("users")
                host = Host().apply {
                    id = 1
                    firstName = "Shota"
                    lastName = "Tsiskaridze"
                }
                for (j in 0 until participants.length()) {
                    val participant = participants.getJSONObject(j)
                    val attendee = Attendee().apply {
                        id = participant.getLong("id")
                        firstName = participant.getString("firstName")
                        lastName = participant.getString("lastName")
                        online = false
                    }
                    attendees?.add(attendee)
                }
            }
            RealmManager.realm.writeBlocking {
                copyToRealm(event)
            }

        }
    }

    suspend fun getEvents(userId: Int): ResponseBody {
        var url = AppData.programURL
        if(url == null){
            RealmManager.realm.writeBlocking {
                val userDetail = this.query<UserDetail>().first()
                url = userDetail.find()?.programUrl.toString()
            }
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(AppData.programURL)
            .client(Certificate().GetClient(AppData.idToken!!))
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        return apiService.getEvents(userId)

    }


}