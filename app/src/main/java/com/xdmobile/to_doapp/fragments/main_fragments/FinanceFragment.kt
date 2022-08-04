package com.xdmobile.to_doapp.fragments.main_fragments

import android.annotation.SuppressLint
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
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.xdmobile.to_doapp.R
import com.xdmobile.to_doapp.adapter.CardViewPagerAdapter
import com.xdmobile.to_doapp.adapter.TransactionsRecyclerAdapter
import com.xdmobile.to_doapp.constants.CardBackground
import com.xdmobile.to_doapp.constants.Tools
import com.xdmobile.to_doapp.database.CardDatabaseHelper
import com.xdmobile.to_doapp.database.DbConstants
import com.xdmobile.to_doapp.database.FinanceDatabaseHelper
import com.xdmobile.to_doapp.databinding.FragmentFincanceBinding
import com.xdmobile.to_doapp.fragments.base.BaseFragment
import com.xdmobile.to_doapp.model.CardModel
import com.xdmobile.to_doapp.model.FinTranModel
import java.time.LocalDate
import java.time.Month
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
class FinanceFragment : BaseFragment<FragmentFincanceBinding>(FragmentFincanceBinding::inflate) {

    private lateinit var financeDatabaseHelper: FinanceDatabaseHelper
    private lateinit var cardsDatabaseHelper: CardDatabaseHelper
    private var finTranModelList = mutableListOf<FinTranModel>()
    private val cardsList = mutableListOf<CardModel>()
    private var userId: Int = -1
    private var cardId: Int = -1
    private var transactionsRecyclerAdapter: TransactionsRecyclerAdapter? = null
    private var cardViewPagerAdapter: CardViewPagerAdapter? = null
    private var filterType = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun initUI(){
        userId = activity?.getSharedPreferences(DbConstants.Preference.NAME, Context.MODE_PRIVATE)!!
            .getInt(DbConstants.Preference.KEY_USER_ID, -1)

        financeDatabaseHelper = FinanceDatabaseHelper(requireContext())
        cardsDatabaseHelper = CardDatabaseHelper(requireContext())

        initCardsList()
        cardViewPagerAdapter = CardViewPagerAdapter(requireContext(), cardsList)
        binding.financeViewPager2.adapter = cardViewPagerAdapter
        binding.wormDotsIndicator.attachTo(binding.financeViewPager2)

        initListeners()
        binding.buttonAll.isChecked = true


        binding.financeViewPager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.i("TAG", "onPageSelected: $position --- $cardId")
                cardId = cardsList[position].id
                Log.i("TAG", "onPageSelected: $position --- $cardId")
//                initFinTransactionsDataToList(financeDatabaseHelper.getDataCursor(cardId))
                when (filterType) {
                    0 -> getAllTransaction()
                    1 -> filterDateWithWeek()
                    2 -> filterDateWithMonth()
                    3 -> filterDateWithYear()
                }
                binding.wormDotsIndicator.setDotIndicatorColor(resources.getColor(cardsList[position].cardStyle.lineColor1))
            }
        })

        transactionsRecyclerAdapter =
            TransactionsRecyclerAdapter(requireContext(), finTranModelList)
        binding.financeRecyclerView.adapter = transactionsRecyclerAdapter
    }

    private fun initCardsList() {
        cardsList.clear()
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
                cardExpenses = cursor.getFloat(8),
                cardReceipts = cursor.getFloat(9)
            )
            cardsList.add(cardModel)
        }
        if (cardsList.isEmpty()) {
            binding.constraintLayout.visibility = View.INVISIBLE
            binding.addEvent.visibility = View.GONE
        } else {
            binding.constraintLayout.visibility = View.VISIBLE
            binding.addEvent.visibility = View.VISIBLE
            binding.wormDotsIndicator.setDotIndicatorColor(resources.getColor(cardsList[0].cardStyle.lineColor1))
        }
    }

    private fun initListeners() {
        binding.financeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            run {
                when (checkedId) {
                    R.id.button_all -> getAllTransaction()
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
        filterDateWithWeek()
    }

    private fun filterDateWithYear() {
        filterType = 3
        if (cardsList.isNotEmpty())
            cardId = cardsList[binding.financeViewPager2.currentItem].id
        Log.i("TAG", "filterDateWithYear: isWorked")
        val localDate = LocalDate.now()
        val year = localDate.year - 1
        val endDate = localDate.toString()
        val startDate = "${year}${endDate.substring(4)}"
        try {
            Log.i("TAG", "filterDateWithWeek: $startDate ---> $endDate")
            val cursor = financeDatabaseHelper.getFilteredDate(startDate, endDate, cardId)
            initFinTransactionsDataToList(cursor)

        } catch (e: SQLiteException) {
            e.stackTrace
        }
    }

    private fun filterDateWithMonth() {
        filterType = 2
        if (cardsList.isNotEmpty())
            cardId = cardsList[binding.financeViewPager2.currentItem].id
        Log.i("TAG", "filterDateWithMonth: isWorked")
        val localDate = LocalDate.now()
        var month = localDate.monthValue - 1
        val endDate = localDate.toString()
        val startDate: String

        if (month > 0) {
            startDate = if (Month.of(month).length(localDate.isLeapYear) >= localDate.dayOfMonth) {
                "${localDate.year}-${getFormattedNumber(month)}-${getFormattedNumber(localDate.dayOfMonth)}"
            } else {
                "${localDate.year}-${getFormattedNumber(month)}-${
                    getFormattedNumber(Month.of(month).length(localDate.isLeapYear))
                }"
            }
        } else {
            month = 12
            startDate = if (Month.of(month).length(localDate.isLeapYear) >= localDate.dayOfMonth) {
                "${localDate.year - 1}-${getFormattedNumber(month)}-${localDate.dayOfMonth}"
            } else {
                "${localDate.year - 1}-${getFormattedNumber(month)}-${
                    Month.of(month).length(localDate.isLeapYear)
                }"
            }
        }


        try {
            Log.i("TAG", "filterDateWithWeek: $startDate ---> $endDate")
            val cursor = financeDatabaseHelper.getFilteredDate(startDate, endDate, cardId)
            initFinTransactionsDataToList(cursor)

        } catch (e: SQLiteException) {
            e.stackTrace
        }
    }

    private fun getFormattedNumber(number: Int): String {
        return if (number / 10 != 0) number.toString() else "0$number"
    }

    private fun filterDateWithWeek() {
        filterType = 1
        if (cardsList.isNotEmpty())
            cardId = cardsList[binding.financeViewPager2.currentItem].id

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
                "${localDate.year}-${getFormattedNumber(month - 1)}-${getFormattedNumber(day)}"
            } else {
                val day = 31 + (currentDayInNumber - 7)
                "${localDate.year - 1}-12-${getFormattedNumber(day)}"
            }
        }

        try {
            Log.i("TAG", "filterDateWithWeek: $startDate ---> $endDate")
            val cursor = financeDatabaseHelper.getFilteredDate(startDate, endDate, cardId)
            initFinTransactionsDataToList(cursor)

        } catch (e: SQLiteException) {
            e.stackTrace
        }
    }

    private fun getAllTransaction() {
        filterType = 0
        if (cardsList.isNotEmpty())
            cardId = cardsList[binding.financeViewPager2.currentItem].id
        try {
            val cursor = financeDatabaseHelper.getDataCursor(cardId)
            initFinTransactionsDataToList(cursor)
        } catch (e: SQLiteException) {
            e.stackTrace
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initFinTransactionsDataToList(cursor: Cursor) {
        Log.i("TAG", "initFinTransactionsDataToList: isWorked")
        var positionEnd = 0
        if (finTranModelList.isNotEmpty())
            positionEnd = finTranModelList.size
        finTranModelList.clear()
        transactionsRecyclerAdapter?.notifyItemRangeRemoved(0, positionEnd)
        val list = mutableListOf<FinTranModel>()
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
                    list.add(finTranModel)
                }
            }
            finTranModelList.addAll(Tools.getTransactionsWithType(list))
            transactionsRecyclerAdapter?.notifyItemInserted(0)

        }
    }

    private fun openCardsFragment() {
        findNavController().navigate(R.id.action_financeFragment_to_cardsFragment)
    }

    private fun showEventDialog() {
        cardId = cardsList[binding.financeViewPager2.currentItem].id
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_event, null)
        val builder = AlertDialog.Builder(requireContext()).apply {
            setView(view)
        }
        val eventCost = view.findViewById<EditText>(R.id.dialog_event_cost)
        val eventName = view.findViewById<EditText>(R.id.dialog_event_name)
        var checkedButtonIndex = 0
        val radioGroup = view.findViewById<RadioGroup>(R.id.dialog_radio_group)
        radioGroup.check(R.id.dialog_button_export)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            run {
                checkedButtonIndex = when (checkedId) {
                    R.id.dialog_button_export -> 0
                    else -> 1
                }
            }
        }

        val dialog = builder.create()

        val buttonSave = view.findViewById<Button>(R.id.dialog_event_save_button)

        buttonSave.setOnClickListener {
            if (eventCost.text.toString().isNotEmpty() && eventName.text.toString().isNotEmpty()) {
                var cost = eventCost.text.toString().toFloat()
                if (checkedButtonIndex == 0 && cardsDatabaseHelper.getCardBalance(cardId) < cost) {
                    Toast.makeText(
                        requireContext(),
                        "There is not enough money on the card",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (checkedButtonIndex == 0)
                        cost *= (-1)
                    val finTranModel = FinTranModel(
                        id = 0,
                        eventCost = cost,
                        userId = userId,
                        addedTime = LocalDate.now().toString(),
                        cardId = cardId,
                        viewType = Tools.VIEW_TYPE_ITEM,
                        eventName = eventName.text.toString()
                    )
                    if (financeDatabaseHelper.insertData(finTranModel)) {
                        Toast.makeText(requireContext(), "Event is saved", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Something went wrong...",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    val currentCard = cardsDatabaseHelper.getCard(cardId)!!
                    currentCard.cardBalance = currentCard.cardBalance + cost
                    if (checkedButtonIndex == 0)
                        currentCard.cardExpenses += abs(cost)
                    else
                        currentCard.cardReceipts += abs(cost)
                    if (cardsDatabaseHelper.updateData(currentCard)) {
                        for (i in 0..cardsList.lastIndex)
                            if (cardsList[i].id == cardId) {
                                cardsList[i].cardBalance = currentCard.cardBalance
                                if (checkedButtonIndex == 0)
                                    cardsList[i].cardExpenses += abs(cost)
                                else
                                    cardsList[i].cardReceipts += abs(cost)
                                cardViewPagerAdapter?.notifyItemChanged(i)
                                break
                            }
                    }

                    dialog.dismiss()
                    finTranModelList.add(finTranModel)
                    when (filterType) {
                        0 -> getAllTransaction()
                        1 -> filterDateWithWeek()
                        2 -> filterDateWithMonth()
                        3 -> filterDateWithYear()
                    }
                    transactionsRecyclerAdapter?.notifyItemInserted(finTranModelList.size - 1)
                }
            }
        }

        dialog.show()
    }
}