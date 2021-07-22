package com.alon.gethandy.Fragments.Business

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alon.gethandy.Activities.BusinessEditProfileActivity
import com.alon.gethandy.Models.Business
import com.alon.gethandy.databinding.FragmentMyBusinessBinding
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MyBusinessFragment(var userEmail: String) : Fragment() {

    private lateinit var business: Business
    private lateinit var binding: FragmentMyBusinessBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyBusinessBinding.inflate(layoutInflater, container, false)

        business = Business()
        db = Firebase.firestore

        binding.bProfileEditBTN.setOnClickListener {
            val intent = Intent(context, BusinessEditProfileActivity::class.java)
            intent.putExtra("business", business)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        fetchUserDetailsFromDB()
        updateUI()
    }

    private fun fetchUserDetailsFromDB() {
        db.collection("businesses").document(userEmail).get().addOnCompleteListener {
            if (it.isSuccessful) {
                business = it.result?.toObject<Business>()!!
                Log.d("pttt", business.toString())
                updateUI()
            } else {
                Log.d("pttt", "business doesn't exist")
            }
        }
    }

    private fun updateUI() {
        // update UI
        Log.d("pttt", "Update UI")
        binding.bProfileOwnerNameTXT.text = business.ownerName
        binding.bProfileLBLEmail.text = business.ownerEmail
        binding.bProfileHoursTXT.text = "${business.startHours} - ${business.endHours}"
        binding.bProfileLBLPhone.text = business.phoneNumber
        //binding.bProfileLBLLocation.text = business.businessAddress
        binding.bProfileDescTXT.text = business.description

        if (business.businessImage.isNotEmpty()) {
            Glide.with(requireContext()).load(business.businessImage).centerCrop()
                .into(binding.bProfileImageIMG)
        }
    }
}