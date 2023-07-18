package edu.iliauni.scheduler

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import edu.iliauni.scheduler.API.ApiService
import edu.iliauni.scheduler.Manager.LayoutManager
import edu.iliauni.scheduler.Manager.RealmManager
import edu.iliauni.scheduler.databinding.ActivityMainBinding
import edu.iliauni.scheduler.objects.Attendee
import edu.iliauni.scheduler.objects.Event
import edu.iliauni.scheduler.objects.Host
import edu.iliauni.scheduler.ui.main.SectionsPagerAdapter
import edu.iliauni.scheduler.ui.main.fragments.CalendarFragment
import edu.iliauni.scheduler.ui.main.fragments.StatisticsFragment
import edu.iliauni.scheduler.ui.main.fragments.TimelineFragment
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Retrofit
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationsButton: ImageView
    private lateinit var filterButton: ImageView

    companion object{
        private const val BASE_URL = "https://134.122.90.22:7237/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val wormDotsIndicator = findViewById<WormDotsIndicator>(R.id.worm_dots_indicator)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        viewPager.setCurrentItem(1)
        wormDotsIndicator.attachTo(viewPager)

        notificationsButton = findViewById(R.id.notifications_btn) as ImageView
        notificationsButton.setOnClickListener {
            val currentIndex = viewPager.currentItem

            when (currentIndex) {
                0 -> LayoutManager(this, StatisticsFragment.binding!!).toggleNotifications()
                1 -> LayoutManager(this, TimelineFragment.binding!!).toggleNotifications()
                2 -> LayoutManager(this, CalendarFragment.binding!!).toggleNotifications()
            }

        }

        filterButton = findViewById(R.id.filter_btn) as ImageView
        filterButton.setOnClickListener {
            val currentIndex = viewPager.currentItem

            when (currentIndex) {
                0 -> LayoutManager(this, StatisticsFragment.binding!!).toggleFilter()
                1 -> LayoutManager(this, TimelineFragment.binding!!).toggleFilter()
                2 -> LayoutManager(this, CalendarFragment.binding!!).toggleFilter()
            }

        }

        val refreshButton = findViewById(R.id.refresh) as ImageView
        refreshButton.setOnClickListener{
            GlobalScope.launch(Dispatchers.IO) {
                try{
                    val responseString = getEvents().string()
                    deleteDb()
                    createDB(responseString)
                }
                catch (exception: Exception){
                    Log.e("exception",exception.toString())
                }
            }
            recreateActivity()
        }
    }

    private fun recreateActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun deleteDb(){
        RealmManager.realm.writeBlocking {
                // fetch all frogs from the realm
                val events = this.query<Event>().find()
                val hosts = this.query<Host>().find()
                val attendeesList = this.query<Attendee>().find()
                // call delete on the results of a query to delete those objects permanently
                delete(attendeesList)
                delete(hosts)
                delete(events)
        }
    }

    suspend fun createDB(responseString: String){
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

    suspend fun getEvents(): ResponseBody {
        // Create a TrustManager that trusts all certificates

        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })

        // Create an SSLContext with the custom TrustManager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, SecureRandom())

        // Create an OkHttpClient that trusts all certificates
        val okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        return apiService.getEvents()

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
