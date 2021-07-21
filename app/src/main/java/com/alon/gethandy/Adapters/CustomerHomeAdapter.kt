package com.alon.gethandy.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alon.gethandy.Activities.BusinessDetailsActivity
import com.alon.gethandy.Models.Business
import com.alon.gethandy.Models.User
import com.alon.gethandy.R
import com.alon.gethandy.databinding.BusinessRowBinding
import com.bumptech.glide.Glide

class CustomerHomeAdapter(private val dataSet: ArrayList<Business>, private val user: User)
    : RecyclerView.Adapter<CustomerHomeAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = BusinessRowBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.business_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.businessRowLBLName.text = dataSet.get(position).businessName
        holder.binding.businessRowLBLAddress.text = dataSet.get(position).businessAddress
        if(dataSet.get(position).numOfRates > 0){
            val rating =
                String.format("%.2f", (dataSet.get(position).totalRating.toDouble() / dataSet.get(position).numOfRates.toDouble()))
            holder.binding.businessRowLBLRating.text =
                rating + " (${dataSet.get(position).numOfRates} rates)"
        } else {
            holder.binding.businessRowLBLRating.text = "0.0 (0 rates)"
        }
        holder.binding.businessRowLBLDistance.text = dataSet.get(position).distance.toString()
        Glide.with(holder.itemView.context).load(dataSet.get(position).businessImage).centerCrop()
            .into(holder.binding.businessRowIMGImage)
        holder.binding.businessRowBTNDetails.setOnClickListener {
            val intent = Intent(holder.itemView.context, BusinessDetailsActivity::class.java)
            intent.putExtra("business", dataSet.get(position))
            intent.putExtra("user", user)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}