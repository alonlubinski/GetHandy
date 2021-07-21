package com.alon.gethandy.Fragments.Other

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alon.gethandy.Adapters.BusinessDetailsReviewsAdapter
import com.alon.gethandy.Models.Review
import com.alon.gethandy.databinding.FragmentBusinessSideReviewsDetailsBinding
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class BusinessSideReviewsDetailsFragment(userEmail: String) : Fragment() {

    private lateinit var db: FirebaseFirestore
    private var reviews = ArrayList<Review>()
    private lateinit var reviewsAdapter: BusinessDetailsReviewsAdapter
    private lateinit var binding: FragmentBusinessSideReviewsDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBusinessSideReviewsDetailsBinding.inflate(inflater, container, false)

        db = Firebase.firestore

        reviewsAdapter = BusinessDetailsReviewsAdapter(reviews)
        binding.businessSideDetailsFragmentReviewsRCV.adapter = reviewsAdapter

        binding.businessSideDetailsFragmentReviewsCHG.setOnCheckedChangeListener { group, checkedId ->
            when (group.findViewById<Chip>(checkedId)?.text) {
                "Date" -> {
                    Log.d("pttt", "Date")
                    var sortedArr = reviews.sortedWith(compareBy { it.reviewDate }).reversed()
                    reviewsAdapter = BusinessDetailsReviewsAdapter(sortedArr)
                }
                "Rating" -> {
                    Log.d("pttt", "Rating")
                    var sortedArr = reviews.sortedWith(compareBy { it.reviewRating }).reversed()
                    reviewsAdapter = BusinessDetailsReviewsAdapter(sortedArr)
                }
            }
            binding.businessSideDetailsFragmentReviewsRCV.adapter = reviewsAdapter
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("pttt", "onResume")
        fetchReviewsFromDb()
    }

    private fun fetchReviewsFromDb() {
        val userEmail = activity?.intent?.getStringExtra("userEmail").toString()


        db.collection("reviews")
            .whereEqualTo("reviewBusinessId", userEmail)
            .get()
            .addOnSuccessListener { documents ->
                var review: Review
                for (document in documents) {
                    review = document.toObject()
                    reviews.add(review)
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                reviewsAdapter = BusinessDetailsReviewsAdapter(reviews)
                binding.businessSideDetailsFragmentReviewsRCV.adapter = reviewsAdapter
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

    }
}