package com.alon.gethandy.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alon.gethandy.Fragments.Other.BusinessDetailsFragment
import com.alon.gethandy.Fragments.Other.BusinessDetailsReviewsFragment
import com.alon.gethandy.Fragments.Other.BusinessLocationFragment
import com.alon.gethandy.R
import com.alon.gethandy.databinding.ActivityBusinessDetailsBinding
import com.google.android.material.tabs.TabLayout

class BusinessDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val detailsFragment = BusinessDetailsFragment()
        val locationFragment = BusinessLocationFragment()
        val reviewsFragment = BusinessDetailsReviewsFragment()
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
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.business_details_FL, fragment)
            commit()
        }
    }
}