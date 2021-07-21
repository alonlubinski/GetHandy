package com.alon.gethandy.Fragments.Other

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import com.alon.gethandy.Adapters.BusinessDetailsReviewsAdapter
import com.alon.gethandy.Models.Review
import com.alon.gethandy.R
import com.alon.gethandy.databinding.AddReviewDialogBinding
import com.alon.gethandy.databinding.FragmentBusinessDetailsReviewsBinding
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BusinessDetailsReviewsFragment : Fragment() {

    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView : View
    private lateinit var dialogBinding: AddReviewDialogBinding
    private lateinit var reviewContentTextField: TextInputLayout
    private lateinit var reviewRatingBar: RatingBar
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = FragmentBusinessDetailsReviewsBinding.inflate(inflater, container, false)

        dialogBinding = AddReviewDialogBinding.inflate(inflater, container, false)

        db = Firebase.firestore


        var arr = ArrayList<Review>()
        // TODO: Handle fetching review from firebase
        arr.add(Review("1", "This is review number one", 3, "20/07/2021", "1",
                        "alon@gmail.com", "Alon Lubinski"))
        arr.add(Review("2", "This is review number twoThis is review number twoThis is review number twoThis is review number twoThis is review number twoThis is review number twoThis is review number twoThis is review number twoThis is review number twoThis is review number twoThis is review number twoThis is review number twoThis is review number two", 4, "21/07/2021", "1",
            "alon@gmail.com", "Alon Lubinski"))
        arr.add(Review("3", "This is review number three", 2, "21/07/2021", "1",
            "alon@gmail.com", "Alon Lubinski"))
        arr.add(Review("4", "This is review number four", 5, "23/07/2021", "1",
            "alon@gmail.com", "Alon Lubinski"))
        arr.add(Review("5", "This is review number five", 1, "24/07/2021", "1",
            "alon@gmail.com", "Alon Lubinski"))
        var adapter = BusinessDetailsReviewsAdapter(arr)
        binding.businessDetailsFragmentReviewsRCV.adapter = adapter

        binding.businessDetailsFragmentReviewsCHG.setOnCheckedChangeListener { group, checkedId ->
            when(group.findViewById<Chip>(checkedId)?.text){
                "Date" -> {
                    Log.d("pttt", "Date")
                    var sortedArr = arr.sortedWith(compareBy { it.reviewDate }).reversed()
                    adapter = BusinessDetailsReviewsAdapter(sortedArr)
                }
                "Rating" -> {
                    Log.d("pttt", "Rating")
                    var sortedArr = arr.sortedWith(compareBy { it.reviewRating }).reversed()
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
                    "1", "alon@gmail.com", "Alon Lubinski")

                // Save review to the db
                newReviewRef.set(review)
                dialog.dismiss()


            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}