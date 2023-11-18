package edu.iliauni.scheduler.Manager

import android.app.Application
import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import edu.iliauni.scheduler.R
import edu.iliauni.scheduler.ui.main.OnSwipeTouchListener

class LayoutManager(private var mContext: Context, private var view: View): Application() {
    private lateinit var list: ListView
    private lateinit var details: LinearLayout

    companion object {
        var eventDetailsVisibleLevel: Int = 0
        var visibleNotifications: Boolean = false
        var visibleSetting: Boolean = false
    }

    fun toggleNotifications(){
        if(eventDetailsVisibleLevel > 0){
            hideEventDetails()
            eventDetailsVisibleLevel = 0
        }
        if(visibleSetting){
            closeSettings()
            visibleSetting = !visibleSetting
        }
        if(!visibleNotifications){
            openNotification()
        }
        else{
            closeNotification()
        }
        visibleNotifications = !visibleNotifications
    }

    fun toggleSettings(){
        if(eventDetailsVisibleLevel > 0){
            hideEventDetails()
            eventDetailsVisibleLevel = 0
        }
        if(visibleNotifications){
            closeNotification()
            visibleNotifications = !visibleNotifications
        }
        if(!visibleSetting)
            openSettings()
        else closeSettings()
        visibleSetting = !visibleSetting
    }

    private fun openNotification(){
        val notificationsLayout = view.findViewById(R.id.notifications_layout) as RelativeLayout
        val anim = R.anim.slide_up
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            1600
        )
        val animation = AnimationUtils.loadAnimation(mContext, anim)
        notificationsLayout.startAnimation(animation)
        notificationsLayout.layoutParams = params

        notificationsLayout.setOnTouchListener(object : OnSwipeTouchListener(mContext),
            View.OnTouchListener {
            override fun onSwipeDown() {
                toggleNotifications()
            }
        })
    }

    private fun closeNotification(){
        val notificationsLayout = view.findViewById(R.id.notifications_layout) as RelativeLayout
        val anim = R.anim.slide_down
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            0
        )
        val animation = AnimationUtils.loadAnimation(mContext, anim)
        notificationsLayout.startAnimation(animation)
        notificationsLayout.layoutParams = params
    }

    private fun openSettings(){
        val settingsLayout = view.findViewById(R.id.settings_layout) as RelativeLayout
        val anim = R.anim.slide_up
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            1600
        )
        val animation = AnimationUtils.loadAnimation(mContext, anim)
        settingsLayout.startAnimation(animation)
        settingsLayout.layoutParams = params

        settingsLayout.setOnTouchListener(object : OnSwipeTouchListener(mContext),
            View.OnTouchListener {
            override fun onSwipeDown() {
                toggleSettings()
            }
        })
    }

    private fun closeSettings(){
        val settingsLayout = view.findViewById(R.id.settings_layout) as RelativeLayout
        val anim = R.anim.slide_down
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            0
        )
        val animation = AnimationUtils.loadAnimation(mContext, anim)
        settingsLayout.startAnimation(animation)
        settingsLayout.layoutParams = params
    }

    fun openEventDetails(){
        if(!visibleNotifications){
            switchEventDetails()
            val eventLayout = view.findViewById(R.id.event_layout) as RelativeLayout
            val anim = R.anim.slide_up
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                1600
            )
            val animation = AnimationUtils.loadAnimation(mContext, anim)
            eventLayout.startAnimation(animation)
            eventLayout.layoutParams = params
            eventDetailsVisibleLevel = 1;
        }
    }

    fun hideEventDetails(){
        if(eventDetailsVisibleLevel == 2)
            minimizeEventDetails()
        else closeEventDetails()
    }

    fun closeEventDetails(){
        val eventLayout = view.findViewById(R.id.event_layout) as RelativeLayout
        val anim = R.anim.double_slide_down
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            0
        )
        val animation = AnimationUtils.loadAnimation(mContext, anim)
        eventLayout.startAnimation(animation)
        eventLayout.layoutParams = params
        eventDetailsVisibleLevel = 0
    }

    fun minimizeEventDetails(){
        switchEventDetails()
        val eventLayout = view.findViewById(R.id.event_layout) as RelativeLayout
        val anim = R.anim.slide_down
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            1600
        )
        val animation = AnimationUtils.loadAnimation(mContext, anim)
        eventLayout.startAnimation(animation)
        eventLayout.layoutParams = params
        eventDetailsVisibleLevel = 1
    }

    fun maximizeEventDetails(){
        switchStudentsList()
        val eventLayout = view.findViewById(R.id.event_layout) as RelativeLayout
        val anim = R.anim.double_slide_up
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        val animation = AnimationUtils.loadAnimation(mContext, anim)
        eventLayout.startAnimation(animation)
        eventLayout.layoutParams = params
        eventDetailsVisibleLevel = 2
    }

    private fun switchStudentsList(){
        details = view.findViewById(R.id.details) as LinearLayout
        list = view.findViewById(R.id.attendees_list) as ListView
        details.visibility = View.GONE
        list.visibility = View.VISIBLE
    }

    private fun switchEventDetails(){
        details = view.findViewById(R.id.details) as LinearLayout
        list = view.findViewById(R.id.attendees_list) as ListView
        details.visibility = View.VISIBLE
        list.visibility = View.GONE
    }
}