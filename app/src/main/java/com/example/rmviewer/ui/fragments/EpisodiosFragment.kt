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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
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

    //Crea un objeto (database) para gestionar la Base de Datos (guardar/leer datos).
    // Conéctata la base de datos de Firebase de ESTE proyecto
    private val database = FirebaseDatabase.getInstance()

    private val firestore = FirebaseFirestore.getInstance()






    private var mostrandoSoloVistos = false

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
                bundle
            )

        }

        // Conectamos el adapter al RecyclerView
        binding.episodiosRecyclerview.adapter = adaptador

        // sellcionar capituos vistos y no vistos
        binding.ivVistos.setOnClickListener {
            mostrandoSoloVistos = true
            actualizarLista()
            botnsVistosResaltado()
        }

        binding.ivTodos.setOnClickListener {
            mostrandoSoloVistos = false
            actualizarLista()
            botnsVistosResaltado()
        }

            // Accedemos al RecyclerView de episodios a través del binding.
            //'addOnScrollListener' añade un "vigilante" que observa cada movimiento de la lista.
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
            api.getEpisodios(paginaActual).
            enqueue(object : Callback<EpisodiosResponse> {

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

                        aplicarVistosDesdeFirestore(listaEpisodios)


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
    //  función para aplicar el estado de 'visto' desde Firebase
    private fun aplicarVistosDesdeFirestore(episodios: List<Episodio>) {

        //SEGURIDAD: Obtenemos el ID del usuario. Si no hay sesión, cancelamos.
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // PETICIÓN: Vamos a la subcolección donde guardamos los episodios del usuario.
        firestore
            .collection("usuarios")
            .document(uid)
            .collection("episodios_vistos")
            .get() // Pedimos la "foto" (snapshot) de todos los documentos ahí guardados.
            .addOnSuccessListener { snapshot ->

                // EXTRACCIÓN: Convertimos la lista de documentos en un Set de IDs.
                // .map { it.id } extrae solo el nombre/ID del documento (ej: "1", "2").
                // .toSet() lo hace más rápido para buscar después.
                val idsVistos = snapshot.documents
                    .map { it.id }
                    .toSet()

                // CRUCE DE DATOS: Comparamos la lista de la API con lo que hay en Firestore.
                for (episodio in episodios) {
                    // Si el ID del episodio de la API existe en nuestro Set de Firestore,
                    // marcamos la propiedad 'visto' como true.
                    episodio.visto = idsVistos.contains(episodio.id.toString())
                }

                // 5. ACTUALIZACIÓN DE INTERFAZ: Enviamos los datos procesados al Adaptador.
                if (mostrandoSoloVistos) {
                    // Si el filtro está activo, solo enviamos los que tienen visto = true.
                    adaptador.setData(episodios.filter { it.visto })
                } else {
                    // Si no, enviamos la lista completa (vistos y no vistos).
                    adaptador.setData(episodios)
                }
            }
            .addOnFailureListener {
                // Es buena práctica manejar errores de conexión aquí.
            }
    }


    //funcion que muestra episodios vistos y no vistos
    private fun actualizarLista() {
        if (mostrandoSoloVistos) {
            adaptador.setData(listaEpisodios.filter { it.visto })
        } else {
            adaptador.setData(listaEpisodios)
        }
    }


    private fun botnsVistosResaltado() {
        if (mostrandoSoloVistos) {
            binding.ivVistos.setBackgroundResource(R.drawable.borde_activo)
            binding.ivTodos.setBackgroundResource(R.drawable.borde_inactivo)
        } else {
            binding.ivVistos.setBackgroundResource(R.drawable.borde_inactivo)
            binding.ivTodos.setBackgroundResource(R.drawable.borde_activo)
        }
    }

}
