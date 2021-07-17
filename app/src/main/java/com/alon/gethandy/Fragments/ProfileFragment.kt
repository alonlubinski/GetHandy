package com.alon.gethandy.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alon.gethandy.Activities.CustomerEditProfileActivity
import com.alon.gethandy.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.profileFABEdit.setOnClickListener{
            val intent = Intent(context, CustomerEditProfileActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // TODO: Get data from firebase and update ui
    }
}