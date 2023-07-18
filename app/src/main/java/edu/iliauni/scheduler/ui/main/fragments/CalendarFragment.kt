package edu.iliauni.scheduler.ui.main.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import edu.iliauni.scheduler.Adapters.EventsListAdapter
import edu.iliauni.scheduler.GoogleForm
import edu.iliauni.scheduler.R
import edu.iliauni.scheduler.Manager.RealmManager
import edu.iliauni.scheduler.objects.Attendee
import edu.iliauni.scheduler.objects.Event
import edu.iliauni.scheduler.objects.Host
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults

class CalendarFragment : Fragment() {
    private lateinit var notificationsButton: ImageView
    private var listener: OnActiveViewChangedListener? = null

    companion object {
        var binding: View? = null
    }

    override fun onResume() {
        super.onResume()
        listener?.onActiveViewChanged(requireView())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflate(R.layout.fragment_calendar, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        displayEventsList(view, requireContext())

        val googleFormsButton = view.findViewById<Button>(R.id.btn_google_forms)
        googleFormsButton.setOnClickListener{
            val intent = Intent(context, GoogleForm::class.java)
            startActivity(intent)
        }

        RealmManager.realm.writeBlocking {
            val hosts = this.query<Host>().find()
            val attendeesList = this.query<Attendee>().find()

        }

        val saveFilterButton = view.findViewById<Button>(R.id.btn_filter_save)
        saveFilterButton.setOnClickListener{

        }
    }

    fun displayEventsList(view: View, context: Context){
        val realmQuery: RealmResults<Event> = RealmManager.realm.query<Event>().find()
        val eventsList: ArrayList<Event> = ArrayList(realmQuery)
        val adapter = EventsListAdapter(activity, eventsList, view, context)
        val listView = view.findViewById(R.id.events_list_calendar) as ListView
        listView?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}