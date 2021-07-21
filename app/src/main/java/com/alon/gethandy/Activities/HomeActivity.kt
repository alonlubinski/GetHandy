package com.alon.gethandy.Activities

import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alon.gethandy.Fragments.Business.BusinessHistoryFragment
import com.alon.gethandy.Fragments.Business.BusinessReviewsFragment
import com.alon.gethandy.Fragments.Business.MyBusinessFragment
import com.alon.gethandy.Fragments.Customer.CustomerHistoryFragment
import com.alon.gethandy.Fragments.Customer.CustomerHomeFragment
import com.alon.gethandy.Fragments.Customer.FavoritesFragment
import com.alon.gethandy.Fragments.Customer.ProfileFragment
import com.alon.gethandy.Models.User
import com.alon.gethandy.R
import com.alon.gethandy.databinding.ActivityHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var customerHomeFragment: CustomerHomeFragment
    private lateinit var customerProfileFragment: ProfileFragment
    private lateinit var customerHistoryFragment: CustomerHistoryFragment
    private lateinit var customerFavoritesFragment: FavoritesFragment

    private lateinit var myBusinessFragment: MyBusinessFragment
    private lateinit var businessHistoryFragment: BusinessHistoryFragment
    private lateinit var businessReviewsFragment: BusinessReviewsFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val userEmail = intent.getStringExtra("userEmail").toString()
        Log.d("pttt", userEmail)

        db = Firebase.firestore

        var user: User
        db.collection("users").document(userEmail).get().addOnCompleteListener {
            if (it.isSuccessful) {
                user = it.result?.toObject<User>()!!
                Log.d("pttt", user.toString())

                // Change bottom menu options according to user type
                if (user.userType == "customer") {
                    binding.homeBNV.inflateMenu(R.menu.menu_customer)
                    customerHomeFragment = CustomerHomeFragment()
                    customerProfileFragment = ProfileFragment(user.email)
                    customerHistoryFragment = CustomerHistoryFragment()
                    customerFavoritesFragment = FavoritesFragment()
                    replaceFragment(customerHomeFragment)
                } else {
                    binding.homeBNV.inflateMenu(R.menu.menu_business)
                    myBusinessFragment = MyBusinessFragment(user.email)
                    businessHistoryFragment = BusinessHistoryFragment()
                    businessReviewsFragment = BusinessReviewsFragment()
                    replaceFragment(myBusinessFragment)
                }
                // Hide progress bar
                binding.homePGB.visibility = INVISIBLE
            } else {
                Log.d("pttt", "User doesn't exist")
            }
        }

        binding.homeBNV.setOnItemSelectedListener {
            when (it.itemId) {
                // Customer options
                R.id.customer_home -> {
                    //Toast.makeText(this, "Customer home", Toast.LENGTH_SHORT).show()
                    replaceFragment(customerHomeFragment)
                    true
                }
                R.id.customer_profile -> {
                    //Toast.makeText(this, "Customer profile", Toast.LENGTH_SHORT).show()
                    replaceFragment(customerProfileFragment)
                    true
                }
                R.id.customer_history -> {
                    //Toast.makeText(this, "Customer history", Toast.LENGTH_SHORT).show()
                    replaceFragment(customerHistoryFragment)
                    true
                }
                R.id.customer_favorites -> {
                    //Toast.makeText(this, "Customer favorites", Toast.LENGTH_SHORT).show()
                    replaceFragment(customerFavoritesFragment)
                    true
                }

                // Business options
//                TODO("check if home is needed")
//                R.id.business_home -> {
//                    Toast.makeText(this, "Business home", Toast.LENGTH_SHORT).show()
//                    true
//                }
                R.id.business_business -> {
                    Toast.makeText(this, "Business business", Toast.LENGTH_SHORT).show()
                    replaceFragment(myBusinessFragment)
                    true
                }
                R.id.business_history -> {
                    Toast.makeText(this, "Business history", Toast.LENGTH_SHORT).show()
                    replaceFragment(businessHistoryFragment)
                    true
                }
                R.id.business_reviews -> {
                    Toast.makeText(this, "Business reviews", Toast.LENGTH_SHORT).show()
                    replaceFragment(businessReviewsFragment)
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

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.home_FL, fragment)
            commit()
        }
    }
}