package com.alon.gethandy.Fragments.Customer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.alon.gethandy.Adapters.CustomerHomeAdapter
import com.alon.gethandy.databinding.FragmentCustomerHomeBinding
import com.google.android.material.appbar.AppBarLayout


class CustomerHomeFragment : Fragment() {


    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = FragmentCustomerHomeBinding.inflate(inflater, container, false)
        var isShow = true
        var scrollRange = -1
        binding.customerHomeABL.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener{ barLayout, verticalOffset ->
            if(scrollRange == -1){
                scrollRange = barLayout?.totalScrollRange!!
            }
            if(verticalOffset * -1 > scrollRange / 5 ){
                binding.customerHomeTIL.visibility = INVISIBLE
                isShow = true
            } else if(isShow){
                binding.customerHomeTIL.visibility = VISIBLE
                isShow = false
            }
        })

        val arr = ArrayList<String>()
        arr.add("One")
        arr.add("Two")
        arr.add("Three")
        arr.add("Four")
        arr.add("Five")
        arr.add("Six")
        var adapter = CustomerHomeAdapter(arr)
        binding.customerHomeRCV.adapter = adapter

        binding.customerHomeEDTSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter = CustomerHomeAdapter(filterDataset(arr, s))
                binding.customerHomeRCV.adapter = adapter
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        return binding.root
    }

    private fun filterDataset(arr: ArrayList<String>, charSequence: CharSequence?) : ArrayList<String> {
        var filteredArr = ArrayList<String>()
        var str = ""
        if(charSequence?.isNotEmpty()!!){
            str = charSequence.subSequence(0, 1).toString().toUpperCase() + charSequence.subSequence(1, charSequence.length)
        }
        for (obj in arr){
            if(obj.startsWith(str)){
                filteredArr.add(obj)
            }
        }
        return filteredArr
    }

}