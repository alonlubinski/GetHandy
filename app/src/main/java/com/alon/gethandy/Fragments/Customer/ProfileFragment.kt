package com.alon.gethandy.Fragments.Customer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alon.gethandy.Activities.CustomerEditProfileActivity
import com.alon.gethandy.Models.User
import com.alon.gethandy.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class ProfileFragment(var userEmail: String) : Fragment() {

    private lateinit var user: User
    private lateinit var binding: FragmentProfileBinding
    private lateinit var db: FirebaseFirestore


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        user = User()

        db = Firebase.firestore

        binding.profileFABEdit.setOnClickListener{
            val intent = Intent(context, CustomerEditProfileActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // TODO: Get data from firebase and update ui
        fetchUserDetailsFromDB()
        updateUI()
    }

    private fun fetchUserDetailsFromDB() {
        db.collection("users").document(userEmail).get().addOnCompleteListener {
            if(it.isSuccessful){
                user = it.result?.toObject<User>()!!
                Log.d("pttt", user.toString())
                updateUI()
            } else {
                Log.d("pttt", "User doesn't exist")
            }
        }
    }

    private fun updateUI() {
        // update UI
        Log.d("pttt", "Update UI")
        binding.profileLBLTitle.text = user.firstName + " " + user.lastName
        binding.profileLBLName.text = user.firstName + " " + user.lastName
        binding.profileLBLPhone.text = user.phone
        binding.profileLBLEmail.text = user.email
        if(user.imageUri.isNotEmpty()){
            Glide.with(requireContext()).load(user.imageUri).centerCrop().into(binding.profileIMGImage)
        }
    }
}