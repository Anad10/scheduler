package edu.iliauni.tabs.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import edu.iliauni.tabs.Manager.LayoutManager
import edu.iliauni.tabs.R
import edu.iliauni.tabs.Manager.RealmManager
import edu.iliauni.tabs.objects.Attendee
import edu.iliauni.tabs.objects.Event
import edu.iliauni.tabs.ui.main.OnSwipeTouchListener
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmList
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventsListAdapter(private var activity: FragmentActivity?, private var items: ArrayList<Event>, private val _view: View, private val context: Context):
    BaseAdapter() {
    private lateinit var eventLayout: RelativeLayout
    private class ViewHolder(row: View?){
        var txtHost: TextView? = null
        var txtEventName: TextView? = null
        var id: Long? = 0
        init{
            this.txtHost = row?.findViewById(R.id.host)
            this.txtEventName = row?.findViewById(R.id.event_name)
        }
    }

    override fun getCount(): Int {
        return items.count()
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val viewHolder: ViewHolder

        if(convertView == null){
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.events_list_item, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        }
        else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        var event = items[position]
        viewHolder.txtHost?.text = event.host?.firstName + " " + event.host?.lastName
        viewHolder.txtEventName?.text = event.name
        viewHolder.id = event.id
        view?.setOnClickListener {
            val startsAt = getTimeFromString(event.startDates.get(0), "yyyy-MM-dd'T'HH:mm:ss")
            val endsAt = getTimeFromString(event.endDates.get(0), "yyyy-MM-dd'T'HH:mm:ss")
            init(event.name, startsAt + " - " + endsAt, event.description)
            LayoutManager(context, _view).openEventDetails()
        }
        return view as View
    }

    fun getTimeFromString(input: String, format: String): String? {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return try {
            val date = dateFormat.parse(input)
            timeFormat.format(date)
        } catch (e: Exception) {
            null
        }
    }

    fun displayAttendeesList(view: View){
        val realmQuery: RealmList<Attendee>? = RealmManager.realm.query<Event>("id == 1").find().first()?.attendees
        val attendeesList: ArrayList<Attendee> = ArrayList(realmQuery)
        val adapter = AttendeesListAdapter(activity, attendeesList)
        val listView = view.findViewById(R.id.attendees_list) as ListView
        listView?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun init(name: String, time: String, description: String){
        eventLayout = activity?.findViewById(R.id.event_layout) as RelativeLayout
        val name_textView = eventLayout.findViewById<TextView>(R.id.event_layout_name)
        val description_textView = eventLayout.findViewById<TextView>(R.id.event_description)
        val time_textView = eventLayout.findViewById<TextView>(R.id.event_time)
        name_textView.text = name
        description_textView.text = description
        time_textView.text = time
        displayAttendeesList(_view)
        eventLayout.setOnTouchListener(object : OnSwipeTouchListener(context),
                    View.OnTouchListener {
                    override fun onSwipeUp() {
                        LayoutManager(context, _view).maximizeEventDetails()
                    }

                    override fun onSwipeDown() {
                        LayoutManager(context, _view).hideEventDetails()
                    }
                })
    }
}