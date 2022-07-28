package com.xdmobile.to_doapp.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.xdmobile.to_doapp.R
import com.xdmobile.to_doapp.adapter.CardRecyclerAdapter
import com.xdmobile.to_doapp.constants.CardBackground
import com.xdmobile.to_doapp.database.CardDatabaseHelper
import com.xdmobile.to_doapp.database.DbConstants
import com.xdmobile.to_doapp.databinding.FragmentCardsBinding
import com.xdmobile.to_doapp.model.CardModel

class CardsFragment : Fragment() {

    private var _binding: FragmentCardsBinding? = null
    private val binding: FragmentCardsBinding get() = _binding!!
    private val cardsList: MutableList<CardModel> = mutableListOf()
    private var cardRecyclerAdapter: CardRecyclerAdapter? = null
    private val itsEmpty = "It's empty"
    private var preferences: SharedPreferences? = null
    private lateinit var cardDatabaseHelper: CardDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCardsBinding.inflate(inflater)
        cardDatabaseHelper = CardDatabaseHelper(requireContext())
        preferences =
            activity?.getSharedPreferences(DbConstants.Preference.NAME, Context.MODE_PRIVATE)


        try {
            val cursor = preferences?.let {
                cardDatabaseHelper.getAllCardsCursor(
                    it.getInt(
                        DbConstants.Preference.KEY_USER_ID,
                        -1
                    )
                )
            }

            if (cursor!!.count < 1) {
                binding.cardRecyclerview.visibility = View.GONE
                binding.imageEmptyCard.visibility = View.VISIBLE
            } else {
                binding.cardRecyclerview.visibility = View.VISIBLE
                binding.imageEmptyCard.visibility = View.GONE
                initDataToList(cursor)
                cardRecyclerAdapter = CardRecyclerAdapter(requireContext(), cardsList)
                binding.cardRecyclerview.adapter = cardRecyclerAdapter!!
            }
        } catch (e: SQLiteException) {
            e.stackTrace
        }

        binding.addCard.setOnClickListener {
            createDialogForCreateCard()
        }


        return binding.root
    }

    private fun createDialogForCreateCard() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_card, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        val dialog = alertDialog.create()

        val cardNameEditText = dialogView.findViewById<EditText>(R.id.dialog_card_name)
        val cardNumberEditText = dialogView.findViewById<EditText>(R.id.dialog_card_number)
        val cardBalanceEditText = dialogView.findViewById<EditText>(R.id.dialog_card_balance)
        val cardDateEditText = dialogView.findViewById<EditText>(R.id.dialog_card_date)
        val createCardButton = dialogView.findViewById<Button>(R.id.dialog_create_card_button)
        val numberRgx = "[0-9]+"

        cardNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val length = s?.length
                if (length == 4 || length == 9 || length == 14) {
                    s.append(" ")
                }
            }
        })


        createCardButton.setOnClickListener {
            val cardNumber = cardNumberEditText.text.toString().trim()
            if (cardNumber.isNotEmpty()) {
                val cardName = cardNameEditText.text.toString().trim()
                if (cardName.isNotEmpty()) {
                    val cardDate = cardDateEditText.text.toString().trim()
                    if (cardDate.isNotEmpty()) {
                        val cardBalance = cardBalanceEditText.text.toString().trim()
                        if (cardBalance.isNotEmpty()) {
                            val cardType =
                                if (cardNumber.startsWith("9860"))
                                    "HUMO"
                                else if (cardNumber.startsWith("8600"))
                                    "UZCARD"
                                else
                                    "UNKNOWN"
                            val cardModel = CardModel(
                                id = 0,
                                cardName = cardName,
                                cardNumber = cardNumber,
                                cardDate = cardDate,
                                cardBalance = cardBalance.toFloat(),
                                cardType = cardType,
                                userId = preferences!!.getInt(
                                    DbConstants.Preference.KEY_USER_ID,
                                    -1
                                ),
                                cardStyle = CardBackground().getCardStyle()
                            )

                            if (isSavedDataToDB(cardModel)) {
                                Toast.makeText(
                                    requireContext(),
                                    "A new card has been added",
                                    Toast.LENGTH_SHORT
                                ).show()
                                cardsList.add(cardModel)
                                cardRecyclerAdapter?.notifyItemInserted(cardsList.size - 1)
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Something went wrong...",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            dialog.dismiss()

                        } else {
                            cardBalanceEditText.error = itsEmpty
                        }
                    } else {
                        cardDateEditText.error = itsEmpty
                    }
                } else {
                    cardNameEditText.error = itsEmpty
                }
            } else {
                cardNumberEditText.error = itsEmpty
            }

        }
        dialog.show()
    }

    private fun isSavedDataToDB(cardModel: CardModel): Boolean {
        return cardDatabaseHelper.insertData(cardModel)
    }

    private fun initDataToList(cursor: Cursor) {
        while (cursor.moveToNext()) {
            val cardModel = CardModel(
                id = cursor.getInt(0),
                cardNumber = cursor.getString(1),
                cardDate = cursor.getString(2),
                cardName = cursor.getString(3),
                cardBalance = cursor.getFloat(4),
                cardType = cursor.getString(5),
                userId = cursor.getInt(6),
                cardStyle = CardBackground().getCardStyleById(cursor.getInt(7))
            )
            cardsList.add(cardModel)
        }
    }

}