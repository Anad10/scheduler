package edu.iliauni.scheduler.ui.main.fragments

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
import edu.iliauni.scheduler.Adapters.EventsListAdapter
import edu.iliauni.scheduler.Adapters.TimelineAdapter
import edu.iliauni.scheduler.BluetoothActivity
import edu.iliauni.scheduler.GoogleForm
import edu.iliauni.scheduler.Manager.RealmManager
import edu.iliauni.scheduler.R
import edu.iliauni.scheduler.Utils.GoogleAuthUtility
import edu.iliauni.scheduler.objects.Event
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

        val bluetoothButton = view.findViewById<Button>(R.id.btn_activate_bluetooth)
        bluetoothButton.setOnClickListener{
            val intent = Intent(context, BluetoothActivity::class.java)
            startActivity(intent)
        }

        val logoutButton = view.findViewById<Button>(R.id.btn_logout)
        logoutButton.setOnClickListener{
            val googleAuth = GoogleAuthUtility.initialize(requireContext())
            if (googleAuth != null) {
                GoogleAuthUtility.singOut(requireContext(), googleAuth)
            }
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