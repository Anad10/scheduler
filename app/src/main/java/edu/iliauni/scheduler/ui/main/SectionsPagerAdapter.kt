package edu.iliauni.scheduler.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import edu.iliauni.scheduler.R
import edu.iliauni.scheduler.ui.main.fragments.CalendarFragment
import edu.iliauni.scheduler.ui.main.fragments.WeeklyFragment
import edu.iliauni.scheduler.ui.main.fragments.TimelineFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_weekly,
    R.string.tab_text_timeline,
    R.string.tab_text_calendar
)
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return WeeklyFragment() //ChildFragment1 at position 0
            1 -> return TimelineFragment() //ChildFragment2 at position 1
            2 -> return CalendarFragment() //ChildFragment3 at position 2
        }
        return TimelineFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 3
    }
}