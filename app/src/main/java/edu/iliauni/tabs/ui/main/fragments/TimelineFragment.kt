package edu.iliauni.tabs.ui.main.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import edu.iliauni.tabs.Adapters.EventsListAdapter
import edu.iliauni.tabs.Adapters.TimelineAdapter
import edu.iliauni.tabs.GoogleForm
import edu.iliauni.tabs.Manager.RealmManager
import edu.iliauni.tabs.R
import edu.iliauni.tabs.objects.Event
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults

class TimelineFragment() : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflate(R.layout.fragment_timeline, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val times = arrayOf<String>().toMutableList()
        for(h: Int in 0..23){
            var hour = h.toString();
            if(h<10)
                hour = "0"+h;
            times.add(hour+":00")
        }
        val adapter = TimelineAdapter(
            activity,
            times,
            view.findViewById(R.id.pointer))

        with(view.findViewById(R.id.timeline_recyclerview) as RecyclerView) {
            this.adapter = adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        adapter.notifyDataSetChanged()
        displayEventsList(view, requireContext())

        val googleFormsButton = view.findViewById<Button>(R.id.btn_google_forms)
        googleFormsButton.setOnClickListener{
            val intent = Intent(context, GoogleForm::class.java)
            startActivity(intent)
        }
    }

    fun displayEventsList(view: View, context: Context){
        val realmQuery: RealmResults<Event> = RealmManager.realm.query<Event>().find()
        val eventsList: ArrayList<Event> = ArrayList(realmQuery)
        val adapter = EventsListAdapter(activity, eventsList, view, context)
        val listView = view.findViewById(R.id.events_list) as ListView
        listView?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    companion object {
        var binding: View? = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}