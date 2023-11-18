package edu.iliauni.scheduler.Utils
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Tasks
import edu.iliauni.scheduler.LoginActivity
import edu.iliauni.scheduler.R
import edu.iliauni.scheduler.config.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
object GoogleAuthUtility {
    private var TAG = "GoogleAuthUtility"
    fun initialize(context: Context): GoogleSignInClient? {
        var googleSignInClient: GoogleSignInClient? = null
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
        return googleSignInClient
    }

    fun signIn(activity: Activity, googleSignInClient: GoogleSignInClient){
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, Constants.RC_SIGN_IN)
    }

    fun singOut(context: Context, googleSignInClient: GoogleSignInClient) {
        val signOutTask = googleSignInClient.signOut()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                Tasks.await(signOutTask, 5000, TimeUnit.MILLISECONDS) // Wait for the sign-out to complete (adjust the timeout as needed)
                if (signOutTask.isSuccessful) {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                } else {
                    Log.e(TAG, "There was an error during sign-out")
                    // You should use runOnUiThread to show the alert dialog on the main thread
                    (context as Activity).runOnUiThread {
                        AlertDialogHelper.showAlert(context, "Error", "There was an error during sign-out")
                    }
                }
            } catch (exception: Exception) {
                Log.e(TAG, exception.message.toString())
            }
        }
    }
}