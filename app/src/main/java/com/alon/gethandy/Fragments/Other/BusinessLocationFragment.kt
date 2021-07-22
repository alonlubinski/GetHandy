package com.alon.gethandy.Fragments.Other

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alon.gethandy.Models.Business
import com.alon.gethandy.R
import com.alon.gethandy.databinding.FragmentBusinessLocationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class BusinessLocationFragment(private val business: Business) : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentBusinessLocationBinding.inflate(inflater, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.business_location_fragment_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        Log.d("pttt","" + business.lat + " + " + business.lon)
        mMap.addMarker(MarkerOptions()
            .position(LatLng(business.lat, business.lon))
            .title(business.businessName))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(business.lat, business.lon), 10f))
    }

}