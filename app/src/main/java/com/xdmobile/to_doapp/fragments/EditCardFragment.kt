package com.xdmobile.to_doapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.xdmobile.to_doapp.R
import com.xdmobile.to_doapp.adapter.CardStyleViewPager2Adapter
import com.xdmobile.to_doapp.constants.CardBackground
import com.xdmobile.to_doapp.database.CardDatabaseHelper
import com.xdmobile.to_doapp.database.DbConstants
import com.xdmobile.to_doapp.databinding.FragmentEditCardBinding
import com.xdmobile.to_doapp.model.CardModel

class EditCardFragment : Fragment() {

    private var _binding: FragmentEditCardBinding? = null
    private val binding: FragmentEditCardBinding get() = _binding!!
    private val cards = mutableListOf<CardModel>()
    private lateinit var cardModel: CardModel
    private var cardStyleId = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditCardBinding.inflate(inflater)
        val bundle = arguments
        cardModel =
            CardDatabaseHelper(requireContext()).getCard(bundle?.getInt(DbConstants.Preference.KEY_CARD_ID)!!)!!

        getCardsStyle()

        val cardStyleViewPager2Adapter = CardStyleViewPager2Adapter(requireContext(), cards)
        binding.editCardViewpager2.adapter = cardStyleViewPager2Adapter
        binding.editCardIndicator.attachTo(binding.editCardViewpager2)
        binding.editCardViewpager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.editCardIndicator.setDotIndicatorColor(resources.getColor(cards[position].cardStyle.lineColor1))
                updateCardStyleId(position)
            }
        })


        binding.editCardSaveButton.setOnClickListener {
            if (CardDatabaseHelper(requireContext()).updateCardStyle(cardModel.id, cardStyleId)) {
                Toast.makeText(
                    requireContext(),
                    "The card style has been updated",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_editCardFragment_to_financeFragment)
            }
        }

        return binding.root
    }

    private fun updateCardStyleId(position: Int) {
        cardStyleId = position + 1
    }

    private fun getCardsStyle() {
        for (i in 1..10)
            cards.add(
                CardModel(
                    0,
                    cardModel.cardName,
                    cardModel.cardBalance,
                    cardModel.cardNumber,
                    cardModel.cardDate,
                    cardModel.cardType,
                    0,
                    CardBackground().getCardStyleById(i)
                )
            )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}