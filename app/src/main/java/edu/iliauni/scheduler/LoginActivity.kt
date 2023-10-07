package edu.iliauni.scheduler

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import edu.iliauni.scheduler.API.ApiService
import edu.iliauni.scheduler.API.Certificate
import edu.iliauni.scheduler.Manager.RealmManager
import edu.iliauni.scheduler.objects.*
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Retrofit

class LoginActivity : AppCompatActivity() {
    companion object{
        private const val RC_SIGN_IN = 562
    }
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Create a GoogleSignInOptions object with the default sign-in scopes
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Create a GoogleSignInClient object with the options specified by gso
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInButton = findViewById<Button>(R.id.btn_signin)
        signInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
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
            println("Signed in successfully!")
            println("ID Token: $idToken")

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
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                catch (exception: Exception){
                    Log.e("exception", exception.toString())
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