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
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding: FragmentCalendarBinding get() = _binding!!
    private lateinit var selectedDate: LocalDate
    private var daysInMonthArray: ArrayList<String> = arrayListOf()
    private var calendarDaysAdapter: CalendarDaysAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater)
        selectedDate = LocalDate.now()

        binding.nextMonth.setOnClickListener {
            nextMonthAction()
        }
        binding.previousMonth.setOnClickListener {
            previousMonthAction()
        }
        setMonthView()
        return binding.root
    }


    private fun setMonthView() {
        binding.monthYearText.text = monthYearFromDate(selectedDate);
        getDaysInMonthArray(selectedDate);
        if (calendarDaysAdapter == null) {
            calendarDaysAdapter = CalendarDaysAdapter(requireContext(), daysInMonthArray)
            binding.calendarRv.adapter = calendarDaysAdapter
        } else {
            calendarDaysAdapter?.notifyItemRangeRemoved(0, daysInMonthArray.size)
            calendarDaysAdapter?.notifyItemInserted(0)
        }
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
}