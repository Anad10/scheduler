package edu.iliauni.tabs.Adapters

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.iliauni.tabs.R


class TimelineAdapter(
    private val mContext: Context?,
    private val timelineList: List<String>,
    private val pointer: ImageView
) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>()
{

    var selectedItem: Int = 0
    var cursorPosition: Int = 0
    var scrollPosition: Int = 0
    lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTextView?.text = timelineList[position]
        holder.mTextView?.setTextColor(ContextCompat.getColor(mContext!!, R.color.dark))
        if(selectedItem == position){
            holder.mTextView?.setTypeface(null, Typeface.BOLD);
        }
        else holder.mTextView?.setTypeface(null, Typeface.NORMAL);

        recyclerView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            scrollPosition = linearLayoutManager.findFirstVisibleItemPosition()
            val desiredPosition = cursorPosition + scrollPosition
            if(selectedItem != desiredPosition){
                selectedItem = desiredPosition
                notifyDataSetChanged()
            }
        }

        var listener = View.OnTouchListener(function = {view, motionEvent ->

            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                if(motionEvent.rawY<=2100) {
                    view.y = motionEvent.rawY - view.height / 2
                    cursorPosition = (motionEvent.rawY / 145).toInt()
                    selectedItem = cursorPosition + scrollPosition
                    notifyDataSetChanged()
                }
            }

            true

        })

        pointer.setOnTouchListener(listener)
    }

    override fun getItemCount() = timelineList.size

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext)
                .inflate(R.layout.recycler_view_list_item, parent, false)
        )
    }

    fun onThumbDrag(){

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTextView: TextView? = view.findViewById(R.id.text_view)
    }
}