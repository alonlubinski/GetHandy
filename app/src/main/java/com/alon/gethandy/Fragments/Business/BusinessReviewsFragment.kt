package com.alon.gethandy.Fragments.Business

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alon.gethandy.Adapters.BusinessDetailsReviewsAdapter
import com.alon.gethandy.Fragments.Other.BusinessSideReviewsDetailsFragment
import com.alon.gethandy.Models.Review
import com.alon.gethandy.R
import com.alon.gethandy.databinding.FragmentBusinessReviewsBinding
import com.alon.gethandy.databinding.FragmentBusinessSideReviewsDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BusinessReviewsFragment(userEmail: String) : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var businessSideReviewsDetailsFragment: BusinessSideReviewsDetailsFragment


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = FragmentBusinessReviewsBinding.inflate(inflater,container,false)
        val userEmail = activity?.intent?.getStringExtra("userEmail").toString()

        businessSideReviewsDetailsFragment = BusinessSideReviewsDetailsFragment(userEmail)
        replaceFragment(businessSideReviewsDetailsFragment)

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.business_reviews_FL, fragment)
            commit()
        }
    }

}