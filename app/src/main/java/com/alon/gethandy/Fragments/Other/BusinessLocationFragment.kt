package com.alon.gethandy.Fragments.Other

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alon.gethandy.R
import com.alon.gethandy.databinding.FragmentBusinessLocationBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment


class BusinessLocationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentBusinessLocationBinding.inflate(inflater, container, false)
        val mapFragment = fragmentManager?.findFragmentById(R.id.business_location_fragment_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(p0: GoogleMap?) {

    }

}