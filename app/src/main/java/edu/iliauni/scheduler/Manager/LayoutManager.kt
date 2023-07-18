package edu.iliauni.scheduler.Manager

import android.app.Application
import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import edu.iliauni.scheduler.R

class LayoutManager(private var mContext: Context, private var view: View): Application() {
    private lateinit var list: ListView
    private lateinit var details: LinearLayout

    companion object {
        var eventDetailsVisibleLevel: Int = 0
        var visibleNotifications: Boolean = false
        var visibleFilter: Boolean = false
    }

    fun toggleNotifications(){
        if(eventDetailsVisibleLevel > 0){
            hideEventDetails()
            eventDetailsVisibleLevel = 0
        }
        if(visibleFilter){
            closeFilter()
            visibleFilter = !visibleFilter
        }
        if(!visibleNotifications){
            openNotification()
        }
        else{
            closeNotification()
        }
        visibleNotifications = !visibleNotifications
    }

    fun toggleFilter(){
        if(eventDetailsVisibleLevel > 0){
            hideEventDetails()
            eventDetailsVisibleLevel = 0
        }
        if(visibleNotifications){
            closeNotification()
            visibleNotifications = !visibleNotifications
        }
        if(!visibleFilter)
            openFilter()
        else closeFilter()
        visibleFilter = !visibleFilter
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

    private fun openFilter(){
        val filterLayout = view.findViewById(R.id.filter_layout) as RelativeLayout
        val anim = R.anim.slide_up
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            1600
        )
        val animation = AnimationUtils.loadAnimation(mContext, anim)
        filterLayout.startAnimation(animation)
        filterLayout.layoutParams = params
    }

    private fun closeFilter(){
        val filterLayout = view.findViewById(R.id.filter_layout) as RelativeLayout
        val anim = R.anim.slide_down
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            0
        )
        val animation = AnimationUtils.loadAnimation(mContext, anim)
        filterLayout.startAnimation(animation)
        filterLayout.layoutParams = params
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