package com.example.rmviewer

import ApiService
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast // Necesario para el mensaje emergente que aparece al hacer click
import androidx.appcompat.view.ActionMode.Callback
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rmviewer.databinding.FragmentEpisodiosBinding
import model.EpisodiosResponse
import network.ApiService
import network.RetrofitClient


class EpisodiosFragment : Fragment() {

    // 1. lateinit: Declaro la variable aquí, pero sé que la inicializaré después
    // dentro de onCreateView. Esto es para tener acceso a los elementos del XML.
    private lateinit var binding: FragmentEpisodiosBinding

    // 2. El controlador de la lista. Se encarga de pintar cada fila.
    private lateinit var adaptador: AdaptadorEpisodios

    // 3. La lista donde guardo los datos reales (los episodios que me da la API)
    //Una colección mutable permite añadir, quitar o modificar elementos después de haber sido creada.
    private val listaEpisodios = mutableListOf<Episodio>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflo el diseño (XML) para que el Fragment lo pueda mostrar.
        binding = FragmentEpisodiosBinding.inflate(inflater, container, false)
        return binding.root // Siempre devuelvo la vista raíz.
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // **********************************************
        // ESTO ES LO CLAVE: LA CONFIGURACIÓN DE LA LISTA
        // **********************************************

        // 1. Configuramos el LayoutManager. Le digo al RecyclerView que se comporte como una
        // lista vertical (como el feed de Instagram, uno debajo de otro).
        binding.episodiosRecyclerview.layoutManager = LinearLayoutManager(requireContext())


        // 2. Inicializo el adaptador.
        adaptador = AdaptadorEpisodios() { episodio ->

            // Este bloque de código `{}` es el "listener".
            // Se ejecuta CADA VEZ que alguien hace click en un elemento de la lista.
            Toast.makeText(
                requireContext(),
                "Click en: ${episodio.name}",
                Toast.LENGTH_SHORT
            ).show()
        }
        // 4. Conecto el adaptador a la lista (al RecyclerView).
        binding.episodiosRecyclerview.adapter = adaptador
        // 3. RETROFIT: Pido la conexión y el servicio de la API.

        // Llamo al Singleton 'instance' que ya configuré antes (el motor de conexión).
        val retrofit = RetrofitClient.instance

        // Le pido a Retrofit que me dé la implementación de mi interfaz (el menú de la API).
        // Con 'apiService' ya puedo hacer llamadas como .getEpisodes().
        val apiService = retrofit.create(ApiService::class.java)

        val call = apiService.getEpisodes(1)

        call.enqueue(object : Callback<EpisodiosResponse> {

        }




    }
}