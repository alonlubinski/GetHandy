package com.alon.gethandy.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alon.gethandy.Activities.BusinessDetailsActivity
import com.alon.gethandy.R
import com.alon.gethandy.databinding.BusinessRowBinding

class CustomerHomeAdapter(private val dataSet: ArrayList<String>) : RecyclerView.Adapter<CustomerHomeAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = BusinessRowBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.business_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.businessRowLBLName.text = dataSet.get(position)
        holder.binding.businessRowBTNDetails.setOnClickListener {
            val intent = Intent(holder.itemView.context, BusinessDetailsActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}