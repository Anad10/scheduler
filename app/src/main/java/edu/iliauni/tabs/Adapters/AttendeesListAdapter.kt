package edu.iliauni.tabs.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import edu.iliauni.tabs.R
import edu.iliauni.tabs.objects.Attendee

class AttendeesListAdapter(private var activity: FragmentActivity?, private var items: ArrayList<Attendee>):
    BaseAdapter() {
    private class ViewHolder(row: View?){
        var txtName: TextView? = null
        var active: ImageView? = null
        init{
            this.txtName = row?.findViewById(R.id.att_name)
            this.active = row?.findViewById(R.id.att_active)
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
            view = inflater.inflate(R.layout.attendees_list_item, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        }
        else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        var attendant = items[position]
        viewHolder.txtName?.text = attendant.firstName + " " + attendant.lastName
        if(attendant.online)
            viewHolder.active?.setImageResource(R.drawable.ic_active)
        else viewHolder.active?.setImageResource(R.drawable.ic_away)
        return view as View
    }
}