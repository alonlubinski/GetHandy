package com.alon.gethandy.Fragments.Customer

import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alon.gethandy.Adapters.CustomerHomeAdapter
import com.alon.gethandy.Models.Business
import com.alon.gethandy.Models.User
import com.alon.gethandy.databinding.FragmentCustomerHomeBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class CustomerHomeFragment(private val user: User) : Fragment() {

    private lateinit var binding: FragmentCustomerHomeBinding
    private lateinit var db: FirebaseFirestore
    private var businesses: ArrayList<Business> = ArrayList()
    private var adapter: CustomerHomeAdapter = CustomerHomeAdapter(businesses, user)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomerHomeBinding.inflate(inflater, container, false)

        db = Firebase.firestore

        var isShow = true
        var scrollRange = -1
        binding.customerHomeABL.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (verticalOffset * -1 > scrollRange / 5) {
                binding.customerHomeLBLTitle.visibility = INVISIBLE
                binding.customerHomeTIL.visibility = INVISIBLE
                isShow = true
            } else if (isShow) {
                binding.customerHomeLBLTitle.visibility = VISIBLE
                binding.customerHomeTIL.visibility = VISIBLE
                isShow = false
            }
        })


        //binding.customerHomeRCV.adapter = adapter

        binding.customerHomeEDTSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    var sortedArr = businesses.sortedWith(compareBy { it.distance })
                    adapter = CustomerHomeAdapter(sortedArr, user)
                } else {
                    adapter = CustomerHomeAdapter(filterDataset(businesses, s), user)
                }
                binding.customerHomeRCV.adapter = adapter
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        fetchBusinessesFromDB()
    }

    private fun filterDataset(
        arr: ArrayList<Business>,
        charSequence: CharSequence?
    ): ArrayList<Business> {
        var filteredArr = ArrayList<Business>()
        var str = ""
        if (charSequence?.isNotEmpty()!!) {
            str = charSequence.subSequence(0, 1).toString()
                .toUpperCase() + charSequence.subSequence(1, charSequence.length)
        }
        for (obj in arr) {
            if (obj.businessName.startsWith(str)) {
                filteredArr.add(obj)
            }
        }
        return filteredArr
    }

    private fun fetchBusinessesFromDB() {
        binding.customerHomeRCV.removeAllViews()
        businesses.clear()
        db.collection("businesses").get().addOnSuccessListener {
            for (document in it) {
                val business = document.toObject<Business>()
                val results = FloatArray(1)
                Location.distanceBetween(user.lat, user.lon, business.lat, business.lon, results)
                business.distance = String.format("%.1f", (results[0] / 1000)) + "KM"
                businesses.add(business)
            }
            var sortedArr = businesses.sortedWith(compareBy { it.distance })
            var adapter = CustomerHomeAdapter(sortedArr, user)
            binding.customerHomeRCV.adapter = adapter
        }
    }

}