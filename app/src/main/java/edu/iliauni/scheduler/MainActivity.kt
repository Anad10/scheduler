package edu.iliauni.scheduler

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
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
import edu.iliauni.scheduler.objects.UserDetail
import edu.iliauni.scheduler.ui.main.SectionsPagerAdapter
import edu.iliauni.scheduler.ui.main.fragments.CalendarFragment
import edu.iliauni.scheduler.ui.main.fragments.StatisticsFragment
import edu.iliauni.scheduler.ui.main.fragments.TimelineFragment
import io.realm.kotlin.Realm
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
        private const val BASE_URL = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RealmManager.realm.writeBlocking {
            // get UserDetails from Realm
            val userDetail = this.query<UserDetail>().find()

            if(userDetail.isEmpty()){
                val intent = Intent(this@MainActivity, ChooseProgramActivity::class.java)
                startActivity(intent)
            }
        }

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

    override fun onDestroy() {
        super.onDestroy()
    }
}
