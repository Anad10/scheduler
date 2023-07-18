package edu.iliauni.tabs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

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
            val email = account?.email
            val idToken = account?.idToken

            // Use the user details as needed
            println("Signed in successfully!")
            //println("Display Name: $displayName")
            println("Email: $email")
            println("ID Token: $idToken")

            // Proceed with sending the ID token to your backend
            sendIdTokenToBackend("idToken")
        } catch (e: ApiException) {
            // Sign in failed, handle the error
            println("Sign in failed: ${e.message}")
        }
    }

    private fun sendIdTokenToBackend(idToken: String?) {
        // Perform API request to send the ID token
        //temporary redirect to ChooseProgramActivity
        val intent = Intent(this, ChooseProgramActivity::class.java)
        startActivity(intent)
    }
}