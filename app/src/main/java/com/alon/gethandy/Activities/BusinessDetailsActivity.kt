package com.alon.gethandy.Activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.alon.gethandy.Fragments.Other.BusinessDetailsFragment
import com.alon.gethandy.Fragments.Other.BusinessDetailsReviewsFragment
import com.alon.gethandy.Fragments.Other.BusinessLocationFragment
import com.alon.gethandy.Models.Business
import com.alon.gethandy.Models.User
import com.alon.gethandy.R
import com.alon.gethandy.databinding.ActivityBusinessDetailsBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BusinessDetailsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityBusinessDetailsBinding
    private lateinit var sharedPref: SharedPreferences
    private var isFavorite: Boolean = false
    private lateinit var favorites: ArrayList<String>
    private var gson: Gson = Gson()
    private lateinit var business: Business
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        business = intent.getSerializableExtra("business") as Business
        user = intent.getSerializableExtra("user") as User

        // Update UI
        updateUI()

        val detailsFragment = BusinessDetailsFragment(business.ownerEmail)
        val locationFragment = BusinessLocationFragment()
        val reviewsFragment = BusinessDetailsReviewsFragment(business, user)
        replaceFragment(detailsFragment)

        binding.businessDetailsTBL.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.contentDescription.toString()){
                    "Details" -> { replaceFragment(detailsFragment) }
                    "Location" -> { replaceFragment(locationFragment) }
                    "Reviews" -> { replaceFragment(reviewsFragment) }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        
        binding.businessDetailsCHBFavorite.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                Log.d("pttt", "checked")
                if(!favorites.contains(business.ownerEmail)){
                    favorites.add(business.ownerEmail)
                }
            } else {
                Log.d("pttt", "unchecked")
                if(favorites.contains(business.ownerEmail)){
                    favorites.remove(business.ownerEmail)
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.business_details_FL, fragment)
            commit()
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("pttt", "onStop")
        var favoritesString = gson.toJson(favorites)
        Log.d("pttt", favoritesString)
        sharedPref.edit().putString("favorites", favoritesString).apply()
    }

    private fun updateUI() {
        sharedPref = this.getSharedPreferences("sp", Context.MODE_PRIVATE)
        var favoritesString = sharedPref.getString("favorites", null)
        Log.d("pttt", favoritesString.toString())

        if(!favoritesString.isNullOrEmpty()){
            var typeToken = object : TypeToken<ArrayList<String>>(){}.type
            favorites = gson.fromJson(favoritesString, typeToken)
            if(favorites.contains(business.ownerEmail)){
                binding.businessDetailsCHBFavorite.isChecked = true
                isFavorite = true
                Log.d("pttt", favorites.size.toString())
            }
        } else {
            favorites = ArrayList()
        }
        Glide.with(this).load(business.businessImage).centerCrop()
            .into(binding.businessDetailsIMGImage)
        binding.businessDetailsLBLName.text = business.businessName
    }
}