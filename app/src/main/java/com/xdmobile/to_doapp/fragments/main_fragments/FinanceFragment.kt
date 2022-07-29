package com.xdmobile.to_doapp.fragments.main_fragments

import android.app.AlertDialog
import android.content.Context
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
import com.xdmobile.to_doapp.adapter.CardViewPagerAdapter
import com.xdmobile.to_doapp.adapter.TransactionsRecyclerAdapter
import com.xdmobile.to_doapp.constants.CardBackground
import com.xdmobile.to_doapp.constants.Tools
import com.xdmobile.to_doapp.database.CardDatabaseHelper
import com.xdmobile.to_doapp.database.DbConstants
import com.xdmobile.to_doapp.database.FinanceDatabaseHelper
import com.xdmobile.to_doapp.databinding.FragmentFincanceBinding
import com.xdmobile.to_doapp.model.CardModel
import com.xdmobile.to_doapp.model.FinTranModel
import java.time.LocalDate
import java.time.Month

@RequiresApi(Build.VERSION_CODES.O)
class FinanceFragment : Fragment() {

    private var _binding: FragmentFincanceBinding? = null
    private val binding: FragmentFincanceBinding get() = _binding!!
    private lateinit var financeDatabaseHelper: FinanceDatabaseHelper
    private lateinit var cardsDatabaseHelper: CardDatabaseHelper
    private var finTranModelList = mutableListOf<FinTranModel>()
    private val cardsList = mutableListOf<CardModel>()
    private var userId: Int = -1
    private var cardId: Int = -1
    private var transactionsRecyclerAdapter: TransactionsRecyclerAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = activity?.getSharedPreferences(DbConstants.Preference.NAME, Context.MODE_PRIVATE)!!
            .getInt(DbConstants.Preference.KEY_USER_ID, -1)

        _binding = FragmentFincanceBinding.inflate(inflater)
        financeDatabaseHelper = FinanceDatabaseHelper(requireContext())
        cardsDatabaseHelper = CardDatabaseHelper(requireContext())

        binding.buttonWeek.isChecked = true

        initListeners()

        initCardsList()

        val cardViewPagerAdapter = CardViewPagerAdapter(requireContext(), cardsList)
        binding.financeViewPager2.adapter = cardViewPagerAdapter
        transactionsRecyclerAdapter =
            TransactionsRecyclerAdapter(requireContext(), finTranModelList)
        binding.financeRecyclerView.adapter = transactionsRecyclerAdapter

        return binding.root
    }

    private fun initCardsList() {
        val cursor = cardsDatabaseHelper.getAllCardsCursor(userId)
        while (cursor.moveToNext()) {
            val cardModel = CardModel(
                id = cursor.getInt(0),
                cardNumber = cursor.getString(1),
                cardDate = cursor.getString(2),
                cardName = cursor.getString(3),
                cardBalance = cursor.getFloat(4),
                cardType = cursor.getString(5),
                userId = cursor.getInt(6),
                cardStyle = CardBackground().getCardStyleById(cursor.getInt(7)),
                cardExpenses = cursor.getFloat(8)
            )
            cardsList.add(cardModel)
        }
    }

    private fun initListeners() {
        binding.financeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
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
    }

    private fun filterDateWithYear() {
        cardId = cardsList[binding.financeViewPager2.currentItem].id
        Log.i("TAG", "filterDateWithYear: isWorked")
        finTranModelList.clear()
        transactionsRecyclerAdapter?.notifyItemRangeRemoved(0, finTranModelList.size)
        val localDate = LocalDate.now()
        val year = localDate.year - 1
        val endDate = localDate.toString()
        val startDate = "${year}${endDate.substring(4)}"
        try {
            val cursor = financeDatabaseHelper.getFilteredDate(startDate, endDate, cardId)
            initDataToList(cursor)
        } catch (e: SQLiteException) {
            e.stackTrace
        }
    }

    private fun filterDateWithMonth() {
        cardId = cardsList[binding.financeViewPager2.currentItem].id
        finTranModelList.clear()
        transactionsRecyclerAdapter?.notifyItemRangeRemoved(0, finTranModelList.size)
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
            val cursor = financeDatabaseHelper.getFilteredDate(startDate, endDate, cardId)
            initDataToList(cursor)
        } catch (e: SQLiteException) {
            e.stackTrace
        }
    }

    private fun filterDateWithWeek() {
        cardId = cardsList[binding.financeViewPager2.currentItem].id
        Log.i("TAG", "filterDateWithWeek: isWorked")
        finTranModelList.clear()
        transactionsRecyclerAdapter?.notifyItemRangeRemoved(0, finTranModelList.size)
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
            val cursor = financeDatabaseHelper.getFilteredDate(startDate, endDate, cardId)
            initDataToList(cursor)
        } catch (e: SQLiteException) {
            e.stackTrace
        }
    }

    private fun initDataToList(cursor: Cursor) {
        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                with(cursor) {
                    val finTranModel = FinTranModel(
                        id = getInt(0),
                        eventCost = getFloat(1),
                        addedTime = getString(2),
                        userId = getInt(3),
                        cardId = getInt(4),
                        viewType = 1,
                        eventName = getString(5)
                    )
                    finTranModelList.add(finTranModel)
                }
            }
            finTranModelList = Tools.getTransactionsWithType(finTranModelList)

        }
    }

    private fun openCardsFragment() {
        findNavController().navigate(R.id.action_financeFragment_to_cardsFragment)
    }

    private fun showEventDialog() {
//        TODO("Hodisa qo'shishi uchun dialog chiqarsin")
        cardId = cardsList[binding.financeViewPager2.currentItem].id
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_event, null)
        val builder = AlertDialog.Builder(requireContext()).apply {
            setView(view)
        }
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}