package com.example.rmviewer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rmviewer.databinding.FragmentDetallesBinding
import com.example.rmviewer.model.Episodio


class DetallesFragment : Fragment() {

    private lateinit var binding: FragmentDetallesBinding
    private var episodio: Episodio? = null   // Objeto recibido del fragment anterior

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetallesBinding.inflate(inflater, container, false) // Inflar layout
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        episodio = arguments?.getParcelable("episodio") // Recuperar el objeto del Bundle

        episodio?.let {
            binding.nombreEpisodio.text = it.name          // Mostrar nombre
            binding.codigoEpisodio.text = it.episode       // Mostrar código
            binding.fechaEmision.text = it.air_date        // Mostrar fecha
            binding.switchVisto.isChecked = it.visto       // Marcar switch según el valor
        }

        //Cada vez que el usuario cambia el Switch:
        //isChecked = true → episodio visto
        //isChecked = false → episodio no visto
        binding.switchVisto.setOnCheckedChangeListener { _, isChecked ->
            episodio?.visto = isChecked
        }
    }
}
