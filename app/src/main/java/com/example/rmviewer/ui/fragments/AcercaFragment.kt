package com.example.rmviewer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.rmviewer.R
import com.example.rmviewer.databinding.FragmentAcercaBinding



class AcercaFragment : Fragment() {


    private lateinit var binding: FragmentAcercaBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {

        binding = FragmentAcercaBinding.inflate(inflater, container, false) // Inflar layout
        return binding.root

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		requireActivity().title = getString(R.string.title_acerca)

        binding.btnCerrar.setOnClickListener { findNavController().navigate(
            R.id.episodiosFragment)

        }
	}


}