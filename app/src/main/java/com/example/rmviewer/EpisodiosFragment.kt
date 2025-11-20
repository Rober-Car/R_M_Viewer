package com.example.rmviewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast // Necesario para el mensaje emergente
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rmviewer.databinding.FragmentEpisodiosBinding

class EpisodiosFragment : Fragment() {

    private lateinit var binding: FragmentEpisodiosBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el diseño (igual que antes)
        binding = FragmentEpisodiosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Configuramos el LayoutManager (la forma de la lista)
        binding.episodiosRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        // --- AQUÍ ESTÁ EL CAMBIO ---

        // A. Creamos una lista de datos FALSA (Hardcoded) para probar
        // Necesitas pasarle valores porque tu data class Episodio lo pide
        val listaDePrueba = listOf(
            Episodio("Episodio 1", R.drawable.hommer, 10),
            Episodio("Episodio 2", R.drawable.fantasma, 5),

        )

        // B. Instanciamos el adaptador pasándole las DOS cosas que necesita:
        //    1. La lista de prueba
        //    2. La función lambda (lo que pasa al hacer click)
        val miAdaptador = AdatadorEpisodios(listaDePrueba) { episodioPulsado ->

            // Esto es lo que ocurre al clicar: Mostramos un mensaje (Toast)
            Toast.makeText(
                requireContext(),
                "Hiciste click en: ${episodioPulsado.name}",
                Toast.LENGTH_SHORT
            ).show()
        }

        // C. Unimos el adaptador al RecyclerView
        binding.episodiosRecyclerview.adapter = miAdaptador
    }
}