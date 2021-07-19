package com.alon.gethandy.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alon.gethandy.Fragments.Customer.CustomerHistoryFragment
import com.alon.gethandy.Fragments.Customer.CustomerHomeFragment
import com.alon.gethandy.Fragments.Customer.FavoritesFragment
import com.alon.gethandy.Fragments.Customer.ProfileFragment
import com.alon.gethandy.databinding.ActivityHomeBinding
import com.alon.gethandy.R

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Change menu options
        //binding.homeBNV.menu.clear()
        //binding.homeBNV.inflateMenu(R.menu.menu_business)
        replaceFragment(CustomerHomeFragment())

        binding.homeBNV.setOnItemSelectedListener {
            when(it.itemId){
                // Customer options
                R.id.customer_home -> {
                    //Toast.makeText(this, "Customer home", Toast.LENGTH_SHORT).show()
                    replaceFragment(CustomerHomeFragment())
                    true
                }
                R.id.customer_profile -> {
                    //Toast.makeText(this, "Customer profile", Toast.LENGTH_SHORT).show()
                    replaceFragment(ProfileFragment())
                    true
                }
                R.id.customer_history -> {
                    //Toast.makeText(this, "Customer history", Toast.LENGTH_SHORT).show()
                    replaceFragment(CustomerHistoryFragment())
                    true
                }
                R.id.customer_favorites -> {
                    //Toast.makeText(this, "Customer favorites", Toast.LENGTH_SHORT).show()
                    replaceFragment(FavoritesFragment())
                    true
                }

                // Business options
                R.id.business_home -> {
                    Toast.makeText(this, "Business home", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.business_business -> {
                    Toast.makeText(this, "Business business", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.business_history -> {
                    Toast.makeText(this, "Business history", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.business_reviews -> {
                    Toast.makeText(this, "Business reviews", Toast.LENGTH_SHORT).show()
                    true
                }

                // Default
                else -> {
                    Toast.makeText(this, "Default", Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.home_FL, fragment)
            commit()
        }
    }
}