package edu.iliauni.tabs.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import edu.iliauni.tabs.Adapters.TimelineAdapter
import edu.iliauni.tabs.GoogleForm
import edu.iliauni.tabs.R

class StatisticsFragment : Fragment() {
    private var listener: OnActiveViewChangedListener? = null

    override fun onResume() {
        super.onResume()
        listener?.onActiveViewChanged(requireView())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflate(R.layout.fragment_statistics, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dates = arrayOf<String>("2023.01.01", "2023.01.07", "2023.01.01", "2023.01.14", "2023.01.21", "2023.01.28",
            "2023.02.04", "2023.02.11", "2023.02.18", "2023.02.25","2023.03.04", "2023.03.11", "2023.03.25",
            "2023.04.01", "2023.04.07", "2023.04.14", "2023.04.21", "2023.04.28", "2023.05.05", "2023.05.12")
        val adapter = TimelineAdapter(
            activity,
            dates.toMutableList(),
            view.findViewById(R.id.pointer))

        with(view.findViewById(R.id.dateline_recyclerview) as RecyclerView) {
            this.adapter = adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

       adapter.notifyDataSetChanged()

        val googleFormsButton = view.findViewById<Button>(R.id.btn_google_forms)
        googleFormsButton.setOnClickListener{
            val intent = Intent(context, GoogleForm::class.java)
            startActivity(intent)
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