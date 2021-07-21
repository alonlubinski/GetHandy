package com.alon.gethandy.Fragments.Other

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import com.alon.gethandy.Adapters.BusinessDetailsReviewsAdapter
import com.alon.gethandy.Adapters.CustomerHomeAdapter
import com.alon.gethandy.Models.Business
import com.alon.gethandy.Models.Review
import com.alon.gethandy.Models.User
import com.alon.gethandy.R
import com.alon.gethandy.databinding.AddReviewDialogBinding
import com.alon.gethandy.databinding.FragmentBusinessDetailsReviewsBinding
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BusinessDetailsReviewsFragment(private val business: Business, private val user: User): Fragment() {

    private lateinit var binding: FragmentBusinessDetailsReviewsBinding
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView : View
    private lateinit var dialogBinding: AddReviewDialogBinding
    private lateinit var reviewContentTextField: TextInputLayout
    private lateinit var reviewRatingBar: RatingBar
    private lateinit var db: FirebaseFirestore
    private var reviews: ArrayList<Review> = ArrayList()
    private var adapter: BusinessDetailsReviewsAdapter = BusinessDetailsReviewsAdapter(reviews)
    private var updatedTotalRating: Int = business.totalRating
    private var updatedNumOfRates: Int = business.numOfRates

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentBusinessDetailsReviewsBinding.inflate(inflater, container, false)

        dialogBinding = AddReviewDialogBinding.inflate(inflater, container, false)

        db = Firebase.firestore

        binding.businessDetailsFragmentReviewsLBLEmpty.visibility = INVISIBLE

        binding.businessDetailsFragmentReviewsCHG.setOnCheckedChangeListener { group, checkedId ->
            val titleOrNull = group.findViewById<Chip>(checkedId)?.text
            when(titleOrNull){
                "Date" -> {
                    Log.d("pttt", "Date")
                    var sortedArr = reviews.sortedWith(compareBy { it.reviewDate }).reversed()
                    adapter = BusinessDetailsReviewsAdapter(sortedArr)
                }
                "Rating" -> {
                    Log.d("pttt", "Rating")
                    var sortedArr = reviews.sortedWith(compareBy { it.reviewRating }).reversed()
                    adapter = BusinessDetailsReviewsAdapter(sortedArr)
                }
            }
            binding.businessDetailsFragmentReviewsRCV.adapter = adapter
        }

        binding.businessDetailsFragmentReviewsBTNAdd.setOnClickListener {
            showAddReviewDialog()
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("pttt", "onResume")
        // TODO: Fetch all reviews from db by business id
        fetchReviewsFromDB()
    }

    private fun showAddReviewDialog() {
        materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        customAlertDialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.add_review_dialog, null, false)

        reviewContentTextField = customAlertDialogView.findViewById(R.id.add_review_TIL_content)
        reviewRatingBar = customAlertDialogView.findViewById(R.id.add_review_RTB)

        materialAlertDialogBuilder.setView(customAlertDialogView)
            .setPositiveButton("Confirm") { dialog, _ ->
                // Generate id from firebase firestore
                val newReviewRef = db.collection("reviews").document()
                val reviewId = newReviewRef.id

                // Get review content and rating from the dialog
                var reviewContent = reviewContentTextField.editText?.text.toString().trim()
                if(reviewContent.isNullOrEmpty()){
                    reviewContent = "No Comment"
                }
                var reviewRating = reviewRatingBar.rating
                if(reviewRating < 1.0f){
                    reviewRating = 1.0f
                }

                // Get the current date
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                val reviewDate = simpleDateFormat.format(Date()).toString()

                // Create new review object
                val review = Review(reviewId, reviewContent, reviewRating.toInt(), reviewDate,
                    business.ownerEmail, user.email, user.firstName + " " + user.lastName, user.imageUri)

                // Save review to the db
                newReviewRef.set(review).addOnSuccessListener {
                    // Update rating in db
                    updatedTotalRating += reviewRating.toInt()
                    updatedNumOfRates += 1
                    db.collection("businesses").document(business.ownerEmail).update(
                        "totalRating", updatedTotalRating, "numOfRates", updatedNumOfRates
                    ).addOnSuccessListener {
                        fetchReviewsFromDB()
                    }
                }

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun fetchReviewsFromDB(){
        binding.businessDetailsFragmentReviewsRCV.removeAllViews()
        reviews.clear()
        binding.businessDetailsFragmentReviewsPGB.visibility = VISIBLE
        db.collection("reviews").whereEqualTo("reviewBusinessId", business.ownerEmail).get()
            .addOnSuccessListener {
                for(document in it){
                    val review = document.toObject<Review>()
                    reviews.add(review)
                }
                if(!reviews.isEmpty()){
                    binding.businessDetailsFragmentReviewsLBLEmpty.visibility = INVISIBLE
                    var adapter = BusinessDetailsReviewsAdapter(reviews)
                    binding.businessDetailsFragmentReviewsRCV.adapter = adapter
                } else {
                    binding.businessDetailsFragmentReviewsLBLEmpty.visibility = VISIBLE
                }
                binding.businessDetailsFragmentReviewsPGB.visibility = INVISIBLE
            }
    }

}