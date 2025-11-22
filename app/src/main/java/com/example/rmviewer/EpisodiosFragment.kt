package com.example.rmviewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rmviewer.databinding.FragmentEpisodiosBinding

// Ajusta estos imports a tus packages reales:
import com.example.rmviewer.Episodio
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
			Toast.makeText(
				requireContext(),
				"Click en: ${episodio.name}",
				Toast.LENGTH_SHORT
			).show()
		}

// Conectamos el adaptador al RecyclerView (vacío inicialmente)
		binding.episodiosRecyclerview.adapter = adaptador



        // ----------------------------
        // BLOQUE: PETICIÓN A LA API (RETROFIT)
        // ----------------------------
		val retrofit = RetrofitClient.instance
		val apiService = retrofit.create(ApiService::class.java)

        // Pedir página 1 de episodios
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
					listaEpisodios.clear()
					listaEpisodios.addAll(episodios)

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
