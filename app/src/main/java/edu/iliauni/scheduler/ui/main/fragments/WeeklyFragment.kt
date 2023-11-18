package edu.iliauni.scheduler.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import edu.iliauni.scheduler.Adapters.TimelineAdapter
import edu.iliauni.scheduler.GoogleForm
import edu.iliauni.scheduler.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WeeklyFragment : Fragment() {
    private var listener: OnActiveViewChangedListener? = null

    override fun onResume() {
        super.onResume()
        listener?.onActiveViewChanged(requireView())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflate(R.layout.fragment_weekly, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val buttonContainer = view.findViewById<LinearLayout>(R.id.button_container)

        // Get the current date
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
        val today = "Today"

        // Create and add 7 buttons for "Today" and the next 6 days
        for (i in 0 until 7) {
            val button = Button(context)
            if (i == 0) {
                button.text = today
            } else {
                calendar.add(Calendar.DATE, 1) // Increment the date for the following days
                button.text = sdf.format(calendar.time) // Format the date
            }
            buttonContainer.addView(button)
        }
    }

    companion object {
        var binding: View? = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}