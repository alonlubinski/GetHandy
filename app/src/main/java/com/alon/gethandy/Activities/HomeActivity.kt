package com.alon.gethandy.Activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var user: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val userEmail = intent.getStringExtra("userEmail").toString()
        Log.d("pttt", userEmail)

        db = Firebase.firestore



        db.collection("users").document(userEmail).get().addOnCompleteListener {
            if (it.isSuccessful) {
                user = it.result?.toObject<User>()!!
                Log.d("pttt", user.toString())

                // Change bottom menu options according to user type
                if (user.userType == "customer") {
                    binding.homeBNV.inflateMenu(R.menu.menu_customer)
                    customerHomeFragment = CustomerHomeFragment(user)
                    customerProfileFragment = ProfileFragment(user.email)
                    customerHistoryFragment = CustomerHistoryFragment()
                    customerFavoritesFragment = FavoritesFragment()
                    replaceFragment(customerHomeFragment)
                } else {
                    binding.homeBNV.inflateMenu(R.menu.menu_business)
                    myBusinessFragment = MyBusinessFragment(user.email)
                    businessHistoryFragment = BusinessHistoryFragment()
                    businessReviewsFragment = BusinessReviewsFragment(user.email)
                    replaceFragment(myBusinessFragment)
                }
                // Hide progress bar
                binding.homePGB.visibility = INVISIBLE
                getLocation()
            } else {
                Log.d("pttt", "User doesn't exist")
            }
        }

        binding.homeBNV.setOnItemSelectedListener {
            when (it.itemId) {
                // Customer options
                R.id.customer_home -> {
                    replaceFragment(customerHomeFragment)
                    true
                }
                R.id.customer_profile -> {
                    replaceFragment(customerProfileFragment)
                    true
                }
                R.id.customer_history -> {
                    replaceFragment(customerHistoryFragment)
                    true
                }
                R.id.customer_favorites -> {
                    replaceFragment(customerFavoritesFragment)
                    true
                }

                //Business
                R.id.business_business -> {
                    replaceFragment(myBusinessFragment)
                    true
                }
                R.id.business_reviews -> {
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



    private fun showDialog(){
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission to access your location is required for better experience with this app")
            setTitle("Permission required")
            setPositiveButton("Ok") { dialog, which ->
                ActivityCompat.requestPermissions(
                    this@HomeActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    101
                )
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
            // Permission not granted
            showDialog()
        } else {
            // Permission granted
            getLocation()
        }
    }


    private fun getLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            if(it != null){
                Log.d("pttt", "lon: ${it.longitude}, lat: ${it.latitude}")
                user.lon = it.longitude
                user.lat = it.latitude
                db.collection("users").document(user.email)
                    .update("lon", user.lon, "lat", user.lat)
                    .addOnSuccessListener {
                        if(user.userType == "customer"){
                            replaceFragment(customerHomeFragment)
                        } else {
                            db.collection("businesses").document(user.email)
                                .update("lon", user.lon, "lat", user.lat)
                                .addOnSuccessListener {
                                    replaceFragment(myBusinessFragment)
                                }
                        }
                    }
            }
        }
    }
}