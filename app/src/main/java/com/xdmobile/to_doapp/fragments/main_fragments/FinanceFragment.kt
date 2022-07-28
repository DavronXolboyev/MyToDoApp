package com.xdmobile.to_doapp.fragments.main_fragments

import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.xdmobile.to_doapp.R
import com.xdmobile.to_doapp.database.FinanceDatabaseHelper
import com.xdmobile.to_doapp.databinding.FragmentFincanceBinding
import com.xdmobile.to_doapp.model.FinTranModel
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class FinanceFragment : Fragment() {

    private var _binding: FragmentFincanceBinding? = null
    private val binding: FragmentFincanceBinding get() = _binding!!
    private lateinit var financeDatabaseHelper: FinanceDatabaseHelper
    private val finTranModelList = mutableListOf<FinTranModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFincanceBinding.inflate(inflater)
        financeDatabaseHelper = FinanceDatabaseHelper(requireContext())

        binding.buttonWeek.isChecked = true

        binding.financeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            run {
                when (checkedId) {
                    R.id.button_week -> filterDateWithWeek()
                    R.id.button_month -> filterDateWithMonth()
                    R.id.button_year -> filterDateWithYear()
                }
            }
        }

        binding.addEvent.setOnClickListener {
            showEventDialog()
        }

        binding.financeCard.setOnClickListener {
            openCardsFragment()
        }

        return binding.root
    }

    private fun filterDateWithYear() {
        Log.i("TAG", "filterDateWithYear: isWorked")
        finTranModelList.clear()
        val localDate = LocalDate.now()
        val year = localDate.year - 1
        val endDate = localDate.toString()
        val startDate = "${year}${endDate.substring(4)}"
        try {
            val cursor = financeDatabaseHelper.getFilteredDate(startDate, endDate)
            initDataToList(cursor)
        } catch (e: SQLiteException) {
            e.stackTrace
        }
    }

    private fun filterDateWithMonth() {
        finTranModelList.clear()
        Log.i("TAG", "filterDateWithMonth: isWorked")
        val localDate = LocalDate.now()
        var month = localDate.monthValue - 1
        val endDate = localDate.toString()
        var startDate = ""

        if (month > 0) {
            startDate = if (Month.of(month).length(localDate.isLeapYear) >= localDate.dayOfMonth) {
                "${localDate.year}-$month-${localDate.dayOfMonth}"
            } else {
                "${localDate.year}-$month-${Month.of(month).length(localDate.isLeapYear)}"
            }
        } else {
            month = 12
            startDate = if (Month.of(month).length(localDate.isLeapYear) >= localDate.dayOfMonth) {
                "${localDate.year - 1}-$month-${localDate.dayOfMonth}"
            } else {
                "${localDate.year - 1}-$month-${Month.of(month).length(localDate.isLeapYear)}"
            }
        }


        try {
            val cursor = financeDatabaseHelper.getFilteredDate(startDate, endDate)
            initDataToList(cursor)
        } catch (e: SQLiteException) {
            e.stackTrace
        }
    }

    private fun filterDateWithWeek() {
        Log.i("TAG", "filterDateWithWeek: isWorked")
        finTranModelList.clear()
        val localDate = LocalDate.now()
        val currentDayInNumber = localDate.dayOfMonth
        val endDate = localDate.toString()
        val startDate = if (currentDayInNumber > 7) {
            "${endDate.substring(0, 8)}${currentDayInNumber - 7}"
        } else {
            val month = localDate.monthValue
            if (month > 1) {
                val day =
                    Month.of(month - 1).length(localDate.isLeapYear) + (currentDayInNumber - 7)
                "${localDate.year}-${month - 1}-$day"
            } else {
                val day = 31 + (currentDayInNumber - 7)
                "${localDate.year - 1}-12-$day"
            }
        }

        try {
            val cursor = financeDatabaseHelper.getFilteredDate(startDate, endDate)
            initDataToList(cursor)
        } catch (e: SQLiteException) {
            e.stackTrace
        }
    }

    private fun initDataToList(cursor: Cursor) {
        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                with(cursor) {
                    // TODO: Bazadan ma'lumot olish
//                    finTranModelList.add(finTranModel)
                }
            }
            TODO("Adapter notify")
        }
    }

    private fun openCardsFragment() {
        findNavController().navigate(R.id.action_financeFragment_to_cardsFragment)
    }

    private fun showEventDialog() {
//        TODO("Hodisa qo'shishi uchun dialog chiqarsin")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}