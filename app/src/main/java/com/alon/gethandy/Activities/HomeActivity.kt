package com.alon.gethandy.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alon.gethandy.databinding.ActivityHomeBinding

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
    }
}