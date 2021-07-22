package com.alon.gethandy.Fragments.Customer

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import com.alon.gethandy.Adapters.CustomerHomeAdapter
import com.alon.gethandy.Models.Business
import com.alon.gethandy.Models.User
import com.alon.gethandy.databinding.FragmentCustomerHistoryBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CustomerHistoryFragment(private val user: User) : Fragment() {

    private lateinit var binding: FragmentCustomerHistoryBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences
    private var historyStrings: ArrayList<String> = ArrayList()
    private var gson: Gson = Gson()
    private var history: ArrayList<Business> = ArrayList()


    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomerHistoryBinding.inflate(inflater, container, false)
        db = Firebase.firestore
        sharedPref = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE)
        binding.historyLBLEmpty.visibility = View.INVISIBLE
        binding.historyPGB.visibility = View.VISIBLE
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        fetchHistoryFromSP()
    }

    private fun fetchHistoryFromSP() {
        sharedPref = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE)
        binding.historyRCV.removeAllViews()
        historyStrings.clear()
        history.clear()
        var historyString = sharedPref.getString("history", null)
        Log.d("pttt", historyString.toString())
        if(!historyString.isNullOrEmpty()){
            var typeToken = object : TypeToken<ArrayList<String>>(){}.type
            historyStrings = gson.fromJson(historyString, typeToken)
            fetchHistoryFromDB()
        } else {
            historyStrings = ArrayList()
            binding.historyLBLEmpty.visibility = View.VISIBLE
            binding.historyPGB.visibility = View.INVISIBLE
        }
    }

    private fun fetchHistoryFromDB(){
        db.collection("businesses").get().addOnSuccessListener {
            for(document in it){
                // Check if business is in history
                val business = document.toObject<Business>()
                if(historyStrings.contains(business.ownerEmail)){
                    // If the business is favorite - add him to the list
                    val results = FloatArray(1)
                    Location.distanceBetween(user.lat, user.lon, business.lat, business.lon, results)
                    business.distance = String.format("%.1f",(results[0] / 1000)) + "KM"
                    history.add(business)
                }
                if(!history.isEmpty()){
                    var adapter = CustomerHomeAdapter(history, User())
                    binding.historyRCV.adapter = adapter
                    binding.historyLBLEmpty.visibility = INVISIBLE
                } else {
                    binding.historyLBLEmpty.visibility = View.VISIBLE
                }
                binding.historyPGB.visibility = INVISIBLE

            }

        }
    }

}