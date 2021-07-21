package com.alon.gethandy.Fragments.Customer

import android.content.Context
import android.content.SharedPreferences
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

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences
    private lateinit var favoritesStrings: ArrayList<String>
    private var gson: Gson = Gson()
    private var favorites: ArrayList<Business> = ArrayList()


    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        db = Firebase.firestore
        sharedPref = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE)

        binding.favoritesLBLEmpty.visibility = INVISIBLE
        binding.favoritesPGB.visibility = VISIBLE

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        fetchFavoritesStringsFromSP()
    }

    private fun fetchFavoritesStringsFromSP(){
        binding.favoritesRCV.removeAllViews()
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