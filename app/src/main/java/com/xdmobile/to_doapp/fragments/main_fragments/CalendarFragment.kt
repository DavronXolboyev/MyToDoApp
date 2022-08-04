package com.xdmobile.to_doapp.fragments.main_fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.xdmobile.to_doapp.R
import com.xdmobile.to_doapp.adapter.CalendarDaysAdapter
import com.xdmobile.to_doapp.databinding.FragmentCalendarBinding
import com.xdmobile.to_doapp.fragments.base.BaseFragment
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(FragmentCalendarBinding::inflate) {

    private lateinit var selectedDate: LocalDate
    private var daysInMonthArray: ArrayList<String> = arrayListOf()
    private var calendarDaysAdapter: CalendarDaysAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun initUI() {
        selectedDate = LocalDate.now()

        binding.nextMonth.setOnClickListener {
            nextMonthAction()
        }
        binding.previousMonth.setOnClickListener {
            previousMonthAction()
        }
        setMonthView()
    }

    private fun setMonthView() {
        binding.monthYearText.text = monthYearFromDate(selectedDate);
        getDaysInMonthArray(selectedDate)
        calendarDaysAdapter = CalendarDaysAdapter(requireContext(), daysInMonthArray)
        calendarDaysAdapter?.notifyItemRangeRemoved(0, daysInMonthArray.size)
        calendarDaysAdapter?.notifyItemInserted(0)
        binding.calendarRv.adapter = calendarDaysAdapter
    }

    private fun getDaysInMonthArray(date: LocalDate){
//        val date = LocalDate.parse("2022-09-08")
        daysInMonthArray.clear()
        val yearMonth = YearMonth.from(date)

        val daysInMonth = yearMonth.lengthOfMonth()
//        println("daysInMonth = $daysInMonth")

        val firstOfMonth: LocalDate = date.withDayOfMonth(1)
//        println("firstOfMonth = $firstOfMonth")

        val dayOfWeek = firstOfMonth.dayOfWeek.value
//        println("dayOfWeek = $dayOfWeek")


        for (i in 1..42) {
            if (i < dayOfWeek || i >= daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek + 1).toString())
            }
        }

    }

    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    private fun previousMonthAction() {
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    private fun nextMonthAction() {
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        calendarDaysAdapter = null
    }
}