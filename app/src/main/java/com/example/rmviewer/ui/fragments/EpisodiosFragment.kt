package com.example.rmviewer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rmviewer.R
import com.example.rmviewer.adapter.AdaptadorEpisodios
import com.example.rmviewer.databinding.FragmentEpisodiosBinding
import com.example.rmviewer.model.Episodio
import com.example.rmviewer.model.EpisodiosResponse
import network.ApiService
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class EpisodiosFragment : Fragment() {

    private lateinit var binding: FragmentEpisodiosBinding
    private lateinit var adaptador: AdaptadorEpisodios

    // ----------------------------
    // VARIABLES DE PAGINACIÓN
    // ----------------------------

    // Página que se está cargando actualmente
    private var paginaActual = 1

    // Indica si la API tiene más páginas
    private var masPaginas: Boolean = true

    // Evita lanzar varias peticiones a la vez
    private var cargando: Boolean = false

    // Lista acumulada de episodios (todas las páginas)
    private val listaEpisodios = mutableListOf<Episodio>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEpisodiosBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // CONFIGURACIÓN DEL RECYCLER

        binding.episodiosRecyclerview.layoutManager =
            LinearLayoutManager(requireContext())

        // Creamos el adaptador SOLO con el listener de click
        // La lista interna la gestiona el propio adaptador
        adaptador = AdaptadorEpisodios { episodio ->

            // Acción al pulsar un episodio
            // Bundle es un contenedor de datos. Sirve para guardar pares clave‑valor y enviarlos
            val bundle = Bundle()

            // Insertar el objeto 'episodio' dentro del BundleUn
            //"episodio" es la clave con la que lo recuperarás en el fragment destino
            //episodio debe implementar Parcelable
            bundle.putParcelable("episodio", episodio)

            //  Navegar hacia 'detallesFragment' pasando el Bundle como argumento
            findNavController().navigate(
                R.id.detallesFragment,
                bundle)

        }

        // Conectamos el adapter al RecyclerView
        binding.episodiosRecyclerview.adapter = adaptador

        binding.episodiosRecyclerview.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {

                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int,
                    dy: Int
                ) {
                    super.onScrolled(recyclerView, dx, dy)

                    // Solo reaccionamos cuando se hace scroll hacia abajo
                    if (dy <= 0) return

                    val layoutManager =
                        recyclerView.layoutManager as LinearLayoutManager

                    // Número total de elementos
                    val totalItems = layoutManager.itemCount

                    // Último elemento visible
                    val lastVisibleItem =
                        layoutManager.findLastVisibleItemPosition()

                    // Si estamos cerca del final, cargamos más
                    if (lastVisibleItem >= totalItems - 3) {
                        cargarEpisodios()
                    }
                }
            }
        )


        // Primera carga de episodios (página 1)
        cargarEpisodios()
    }


    // FUNCIÓN QUE CARGA UNA PÁGINA DE EPISODIOS
    fun cargarEpisodios() {

        // ===========================================
        // BLOQUE CONFIGURACIÓN DE RETROFIT
        // Creamos el "servicio" a partir de la interfaz ApiService
        val api = RetrofitClient.instance.create(ApiService::class.java)

        // ===========================================
        // BLOQUE DE CONTROL DE PAGINACIÓN
        // Solo llamamos a la API si:
        // - Hay más páginas
        // - No se está cargando ya otra petición
        if (masPaginas && !cargando) {

            // Marcamos que empieza la carga
            cargando = true

            // Ejecutamos la llamada a la API de forma asíncrona
            api.getEpisodios(paginaActual).enqueue(object : Callback<EpisodiosResponse> {

                // -----------------------------------------
                // RESPUESTA CORRECTA DEL SERVIDOR
                // -----------------------------------------
                override fun onResponse(
                    call: Call<EpisodiosResponse>,
                    response: Response<EpisodiosResponse>
                ) {
                    // Comprobamos que la respuesta sea válida
                    if (response.isSuccessful && response.body() != null) {

                        // Respuesta completa del JSON
                        val respuesta = response.body()!!

                        // Lista de episodios de ESTA página
                        val episodios = respuesta.results

                        //acumulamos los episodiso de cada pagina
                        listaEpisodios.addAll(episodios)

                        // Actualizamos el adapter (por ahora REEMPLAZA la lista)
                        adaptador.setData(listaEpisodios)

                        // Comprobamos si hay más páginas mirando info.next
                        masPaginas = respuesta.info.next != null

                        // Si hay más páginas, avanzamos
                        if (masPaginas) {
                            paginaActual++
                        }
                    }

                    // Marcamos que la carga ha terminado
                    cargando = false
                }

                // -----------------------------------------
                // ERROR DE RED
                // -----------------------------------------
                override fun onFailure(call: Call<EpisodiosResponse>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Fallo de red: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()

                    // Marcamos que la carga ha terminado
                    cargando = false
                }
            })
        }
    }
}
