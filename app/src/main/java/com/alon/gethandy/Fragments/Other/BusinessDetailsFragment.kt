package com.alon.gethandy.Fragments.Other

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alon.gethandy.Models.Business
import com.alon.gethandy.databinding.FragmentBusinessDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class BusinessDetailsFragment(private val businessOwnerEmail: String) : Fragment() {

    private lateinit var binding: FragmentBusinessDetailsBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var business: Business

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentBusinessDetailsBinding.inflate(inflater, container, false)
        db = Firebase.firestore
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        db.collection("businesses").document(businessOwnerEmail).get().addOnSuccessListener {
            business = it.toObject<Business>()!!
            updateUI()
        }
    }

    private fun updateUI() {
        binding.businessDetailsFragmentLBLName.text = business.businessName
        binding.businessDetailsFragmentLBLOwner.text = business.ownerName
        binding.businessDetailsFragmentLBLPhone.text = business.phoneNumber
        binding.businessDetailsFragmentLBLEmail.text = business.ownerEmail
        binding.businessDetailsFragmentLBLAddress.text = business.businessAddress
        binding.businessDetailsFragmentLBLHours.text = business.startHours + " - " + business.endHours
        if(business.numOfRates > 0) {
            binding.businessDetailsFragmentLBLRating.text =
                (business.totalRating.toDouble() / business.numOfRates.toDouble()).toString() +
                        " (${business.numOfRates} rates)"
        } else {
            binding.businessDetailsFragmentLBLRating.text = "0"
        }
        binding.businessDetailsFragmentLBLDescription.text = business.description

    }

}