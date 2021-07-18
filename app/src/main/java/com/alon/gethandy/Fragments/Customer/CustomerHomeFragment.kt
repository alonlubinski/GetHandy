package com.alon.gethandy.Fragments.Customer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
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
        return binding.root
    }

}