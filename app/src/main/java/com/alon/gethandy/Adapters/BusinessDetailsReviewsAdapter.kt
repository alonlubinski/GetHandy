package com.alon.gethandy.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alon.gethandy.Models.Review
import com.alon.gethandy.R
import com.alon.gethandy.databinding.ReviewRowBinding
import com.bumptech.glide.Glide

class BusinessDetailsReviewsAdapter(private val dataSet: List<Review>)
    : RecyclerView.Adapter<BusinessDetailsReviewsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ReviewRowBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.binding.reviewRowIMGImage
        Glide.with(holder.itemView.context).load(dataSet.get(position).reviewUserImageUri).centerCrop()
            .into(holder.binding.reviewRowIMGImage)
        holder.binding.reviewRowLBLName.text = dataSet.get(position).reviewUserName
        holder.binding.reviewRowLBLDate.text = dataSet.get(position).reviewDate
        holder.binding.reviewRowLBLContent.text = dataSet.get(position).reviewContent
        holder.binding.reviewRowRTB.rating = dataSet.get(position).reviewRating.toFloat()
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}