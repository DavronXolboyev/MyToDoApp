package com.xdmobile.to_doapp.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import com.xdmobile.to_doapp.R
import com.xdmobile.to_doapp.adapter.CardRecyclerAdapter
import com.xdmobile.to_doapp.constants.CardBackground
import com.xdmobile.to_doapp.database.CardDatabaseHelper
import com.xdmobile.to_doapp.database.DbConstants
import com.xdmobile.to_doapp.database.FinanceDatabaseHelper
import com.xdmobile.to_doapp.databinding.FragmentCardsBinding
import com.xdmobile.to_doapp.model.CardModel
import org.w3c.dom.Text

class CardsFragment : Fragment(), CardRecyclerAdapter.OnLongClickListener {

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
            val cursor = cardDatabaseHelper.getAllCardsCursor(
                preferences!!.getInt(
                    DbConstants.Preference.KEY_USER_ID,
                    -1
                )
            )

            if (cursor.count < 1) {
                setVisibility()
            } else {
                setVisibility(true)
                initDataToList(cursor)
                cardRecyclerAdapter = CardRecyclerAdapter(requireContext(), cardsList)
                binding.cardRecyclerview.adapter = cardRecyclerAdapter!!
                cardRecyclerAdapter?.setOnLongClickListener(this)
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

        cardBalanceEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (cardBalanceEditText.text.toString().startsWith("0")
                    && cardBalanceEditText.text.length > 1
                ) {

                    cardBalanceEditText.setText("0");
                    cardBalanceEditText.setSelection(cardBalanceEditText.text.toString().length);
                }
            }
        })

        createCardButton.setOnClickListener {
            val cardNumber = cardNumberEditText.text.toString().trim()
            if (cardNumber.length == 16) {
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
                                if (cardsList.size == 1) {
                                    setVisibility(true)
                                    cardRecyclerAdapter =
                                        CardRecyclerAdapter(requireContext(), cardsList)
                                    binding.cardRecyclerview.adapter = cardRecyclerAdapter
                                } else
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
                cardStyle = CardBackground().getCardStyleById(cursor.getInt(7)),
                cardExpenses = cursor.getFloat(8)
            )
            cardsList.add(cardModel)
        }
    }

    private fun setVisibility(isVisible: Boolean = false) {
        if (isVisible) {
            binding.cardRecyclerview.visibility = View.VISIBLE
            binding.imageEmptyCard.visibility = View.GONE
        } else {
            binding.cardRecyclerview.visibility = View.GONE
            binding.imageEmptyCard.visibility = View.VISIBLE
        }
    }

    override fun onLongClick(position: Int, view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu_card, popupMenu.menu)
        popupMenu.setForceShowIcon(true)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.popup_menu_edit_card -> {
                    openEditCardGFragment(position)
                }
                R.id.popup_menu_delete_card -> {
                    showAlertDialog(position)
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun openEditCardGFragment(position: Int) {
        val bundle = Bundle()
        bundle.putInt(DbConstants.Preference.KEY_CARD_ID, cardsList[position].id)
        findNavController().navigate(R.id.action_cardsFragment_to_editCardFragment, bundle)
    }

    private fun showAlertDialog(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
            .setIcon(R.drawable.ic_baseline_delete_24)
            .setTitle("Delete")
            .setMessage("Are you sure you want to delete the card?")
            .setPositiveButton(
                "Yes"
            ) { dialog, _ ->
                val ftDb = FinanceDatabaseHelper(requireContext())
                if (cardDatabaseHelper.deleteCard(cardsList[position].id) && ftDb.deleteRow(
                        cardsList[position].id
                    )
                ) {
                    cardsList.removeAt(position)
                    cardRecyclerAdapter?.notifyItemRemoved(position)
                    Toast.makeText(requireContext(), "Something went wrong...", Toast.LENGTH_SHORT)
                        .show()
                }
                dialog?.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog?.dismiss()
            }
        builder.create().show()
    }
}