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
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.alon.gethandy.Adapters.CustomerHomeAdapter
import com.alon.gethandy.Models.Business
import com.alon.gethandy.Models.User
import com.alon.gethandy.databinding.FragmentFavoritesBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoritesFragment(private val user: User) : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences
    private var favoritesStrings: ArrayList<String> = ArrayList()
    private var gson: Gson = Gson()
    private var favorites: ArrayList<Business> = ArrayList()


    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        db = Firebase.firestore


        binding.favoritesLBLEmpty.visibility = INVISIBLE
        binding.favoritesPGB.visibility = VISIBLE

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("pttt", "onResume")
        fetchFavoritesStringsFromSP()
    }

    private fun fetchFavoritesStringsFromSP(){
        sharedPref = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE)
        binding.favoritesRCV.removeAllViews()
        favoritesStrings.clear()
        favorites.clear()
        var favoritesString = sharedPref.getString("favorites", null)
        Log.d("pttt", favoritesString.toString())
        if(!favoritesString.isNullOrEmpty()){
            var typeToken = object : TypeToken<ArrayList<String>>(){}.type
            favoritesStrings = gson.fromJson(favoritesString, typeToken)
            fetchFavoritesFromDB()
        } else {
            favoritesStrings = ArrayList()
            binding.favoritesLBLEmpty.visibility = VISIBLE
            binding.favoritesPGB.visibility = INVISIBLE
        }
    }

    private fun fetchFavoritesFromDB(){
        db.collection("businesses").get().addOnSuccessListener {
            for(document in it){
                // Check if business is favorite
                val business = document.toObject<Business>()
                if(favoritesStrings.contains(business.ownerEmail)){
                    // If the business is favorite - add him to the list
                    val results = FloatArray(1)
                    Location.distanceBetween(user.lat, user.lon, business.lat, business.lon, results)
                    business.distance = String.format("%.1f",(results[0] / 1000)) + "KM"
                    favorites.add(business)
                }
                if(!favorites.isEmpty()){
                    var adapter = CustomerHomeAdapter(favorites, User())
                    binding.favoritesRCV.adapter = adapter
                } else {
                    binding.favoritesLBLEmpty.visibility = VISIBLE
                }
                binding.favoritesPGB.visibility = INVISIBLE

            }

        }
    }

}