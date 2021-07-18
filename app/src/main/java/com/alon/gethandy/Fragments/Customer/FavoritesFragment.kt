package com.alon.gethandy.Fragments.Customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alon.gethandy.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {




    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        return binding.root
    }

}