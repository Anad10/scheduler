package edu.iliauni.scheduler

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import edu.iliauni.scheduler.Manager.LayoutManager
import edu.iliauni.scheduler.Manager.RealmManager
import edu.iliauni.scheduler.databinding.ActivityMainBinding
import edu.iliauni.scheduler.objects.*
import edu.iliauni.scheduler.ui.main.SectionsPagerAdapter
import edu.iliauni.scheduler.ui.main.fragments.CalendarFragment
import edu.iliauni.scheduler.ui.main.fragments.WeeklyFragment
import edu.iliauni.scheduler.ui.main.fragments.TimelineFragment
import io.realm.kotlin.ext.query


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationsButton: ImageView
    private lateinit var settingsButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RealmManager.realm.writeBlocking {
            // get UserDetails from Realm
            val userDetail = this.query<UserDetail>().first()
            val url = userDetail.find()?.programUrl

            if (url == null) {
                val intent = Intent(this@MainActivity, ChooseProgramActivity::class.java)
                startActivity(intent)
            }
            else {
                AppData.programURL = url
                AppData.idToken = userDetail.find()?.idToken.toString()
                val acct = GoogleSignIn.getLastSignedInAccount(this@MainActivity)
                if (acct == null) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else if(AppData.idToken == null) {
                    AppData.idToken = acct.idToken.toString()
                }
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
                0 -> LayoutManager(this, WeeklyFragment.binding!!).toggleNotifications()
                1 -> LayoutManager(this, TimelineFragment.binding!!).toggleNotifications()
                2 -> LayoutManager(this, CalendarFragment.binding!!).toggleNotifications()
            }

        }

        settingsButton = findViewById(R.id.settings_btn) as ImageView
        settingsButton.setOnClickListener {
            val currentIndex = viewPager.currentItem

            when (currentIndex) {
                0 -> LayoutManager(this, WeeklyFragment.binding!!).toggleSettings()
                1 -> LayoutManager(this, TimelineFragment.binding!!).toggleSettings()
                2 -> LayoutManager(this, CalendarFragment.binding!!).toggleSettings()
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
