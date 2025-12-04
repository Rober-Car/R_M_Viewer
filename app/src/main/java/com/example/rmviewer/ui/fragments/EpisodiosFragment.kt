package com.example.rmviewer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rmviewer.R
import com.example.rmviewer.adapter.AdaptadorEpisodios
import com.example.rmviewer.databinding.FragmentEpisodiosBinding

// Ajusta estos imports a tus packages reales:
import model.Episodio
import model.EpisodiosResponse
import network.RetrofitClient
import network.ApiService

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EpisodiosFragment : Fragment() {

    private lateinit var binding: FragmentEpisodiosBinding
    private lateinit var adaptador: AdaptadorEpisodios
    private val listaEpisodios = mutableListOf<Episodio>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEpisodiosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ---------------------------
        // CONFIGURACIÓN RECYCLER
        // ---------------------------
        binding.episodiosRecyclerview.layoutManager =
            LinearLayoutManager(requireContext())

        adaptador = AdaptadorEpisodios { episodio ->
            Toast.makeText(requireContext(), "Click en: ${episodio.name}", Toast.LENGTH_SHORT)
                .show()

        }

           // ----------------------------
        // BLOQUE: CONFIGURACIÓN INICIAL
        // ----------------------------
        binding.episodiosRecyclerview.layoutManager =
            LinearLayoutManager(requireContext())


        // Creamos el adaptador solo con el listener (la lista interna la gestiona el adaptador)
        adaptador = AdaptadorEpisodios { episodio ->
            // ----------------------------
            // BLOQUE: CLICK EN ITEM
            // ----------------------------

            findNavController().navigate(R.id.detallesFragment)

        }

        // Conectamos el adaptador al RecyclerView (vacío inicialmente)
        binding.episodiosRecyclerview.adapter = adaptador


        // ===========================================
        // BLOQUE CONFIGURACIÓN DE RETROFIT
        // El objetivo de este bloque es crear un "servicio" ejecutable
        // a partir de la interfaz de la API que definimos (ApiService).
        // ===========================================--------------------------

        // Obtenemos la instancia de Retrofit que configuramos en RetrofitClient
        // 'RetrofitClient.instance' contiene la configuración base para todas las peticiones:
        // - La URL base de la API (ej: https://rickandmortyapi.com/api/).
        // - El conversor JSON (Gson o Moshi) para transformar respuestas HTTP a objetos Kotlin.
        val retrofit = RetrofitClient.instance

        // Retrofit toma la interfaz 'ApiService' (el plano de las peticiones)
        // y genera automáticamente una clase funcional ('apiService')
        // que traduce las llamadas Kotlin a peticiones HTTP reales a la web.
        val apiService = retrofit.create(ApiService::class.java)

        // ===========================================
        // BLOQUE 2: EJECUCIÓN DE LA PETICIÓN
        // ===========================================
        // Al llamar a 'getEpisodes(1)', Retrofit construye el objeto 'Call'.
        // Este objeto 'Call' representa la petición HTTP específica,
        // que en este caso será algo como: GET /api/episode?page=1
        // IMPORTANTE: En este punto, la petición AÚN NO se ha enviado a la web.
        // Solo se ha PREPARADO para ser ejecutada.
        val call = apiService.getEpisodes(1)

        call.enqueue(object : Callback<EpisodiosResponse> {

            override fun onResponse(
                call: Call<EpisodiosResponse>,
                response: Response<EpisodiosResponse>
            ) {

                // ----------------------------
                // BLOQUE: RESPUESTA OK
                // ----------------------------
                if (response.isSuccessful && response.body() != null) {

                    val episodios = response.body()!!.results   // List<Episodio>

                    // ----------------------------
                    // BLOQUE: ACTUALIZAR LISTA LOCAL (opcional)
                    // ----------------------------
                    listaEpisodios.clear()       // Limpia la lista anterior para evitar duplicados.
                    listaEpisodios.addAll(episodios) // Agrega los nuevos episodios obtenidos de la API

                    // ----------------------------
                    // BLOQUE: ACTUALIZAR ADAPTADOR (REFRESCA UI)
                    // ----------------------------
                    adaptador.setData(episodios)

                } else {

                    // ----------------------------
                    // BLOQUE: ERROR HTTP
                    // ----------------------------
                    Toast.makeText(
                        requireContext(),
                        "Error HTTP: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<EpisodiosResponse>, t: Throwable) {

                // ----------------------------
                // BLOQUE: ERROR DE RED
                // ----------------------------
                Toast.makeText(
                    requireContext(),
                    "Fallo de red: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}
