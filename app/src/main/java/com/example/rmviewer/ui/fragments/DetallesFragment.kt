package com.example.rmviewer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rmviewer.R
import com.example.rmviewer.adapter.AdaptadorPersonajes
import com.example.rmviewer.databinding.FragmentDetallesBinding
import com.example.rmviewer.model.Episodio
import com.example.rmviewer.model.Personaje
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import network.ApiService
import network.RetrofitClient
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response


class DetallesFragment : Fragment() {

    private lateinit var personajesAdapter: AdaptadorPersonajes
    private lateinit var binding: FragmentDetallesBinding

    // Objeto recibido del fragment anterior
    private var episodio: Episodio? = null

    //  Inicializa la conexión con el servicio de Autenticación de Firebase
    private val auth = FirebaseAuth.getInstance()

    // Obtiene el UID (identificador único) del usuario que ha iniciado sesión actualmente.
    //Se usa '!!' porque se asume que el usuario SIEMPRE está logueado en este punto y no puede ser null
    private val uid = auth.currentUser!!.uid

    //Crea un objeto (database) para gestionar la Base de Datos (guardar/leer datos).
    // Conéctata la base de datos de Firebase de ESTE proyecto
    private val database = FirebaseDatabase.getInstance()


    // Crea una referencia que apunta al nodo principal "Usuarios" en la base de datos de Firebase.
    // Si no existe, Firebase la creará cuando escribas algo
    private val usuariosRef = database.getReference("usuarios")

    private val firestore = FirebaseFirestore.getInstance()

    //Es la función más importante. Su único trabajo es "inflar" el diseño XML
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetallesBinding.inflate(inflater, container, false) // Inflar layout
        return binding.root
    }


    //(El Cerebro)
    //Esta es la función donde ocurre la magia. Se ejecuta justo después de que la vista se ha creado.
    // Es el lugar más seguro para configurar botones, listas y observadores.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Titulo del toolbar
        requireActivity().title = getString(R.string.title_detalles)

        // Recupera el objeto 'Episodio' que fue enviado a este fragmento/actividad.
        episodio = arguments?.getParcelable("episodio")

        // Asegura que el objeto 'episodio' no es nulo antes de intentar usarlo.
        episodio?.let {
            // // Asigna la cada propiedad a su view
            binding.nombreEpisodio.text = it.name          // Mostrar nombre
            binding.codigoEpisodio.text = it.episode       // Mostrar código
            binding.fechaEmision.text = it.air_date        // Mostrar fecha
            binding.switchVisto.isChecked = it.visto       // Marcar switch según el valor
        }

        //Cada vez que el usuario cambia el Switch:
        // _ → ignoro el primer parámetro
        //isChecked → me quedo solo con el booleano que necesito
        binding.switchVisto.setOnCheckedChangeListener { _, isChecked ->

            // ? Le dice al compilador: Solo continúa con lo que sigue si el objeto  no es null
            //let es una de las funciones de ámbito
            // Función: Ejecuta el bloque de código definido dentro de sus llaves ({ ... })
            // solo si el operador ?. permitió la llamada (es decir, si episodio no era null)
            // usando la referencia segura ep, asigna el valor del Switch (isChecked)a la propiedad .visto.
            episodio?.let { ep ->
                ep.visto = isChecked

                actualizarEpisodioVisto(ep,ep.visto)

            }
        }

        //---------CONFIGURANDO EL ADAPATADOR
        personajesAdapter = AdaptadorPersonajes { personaje ->
        }
        binding.personajesRecyclerview.layoutManager =
            GridLayoutManager(requireContext(), 2)

        binding.personajesRecyclerview.adapter = personajesAdapter
        cargarPersonajes()

    }

    private fun actualizarEpisodioVisto(ep: Episodio, visto: Boolean) {

        //  OBTENER IDENTIFICADOR: Verificamos que el usuario esté autenticado.
        // Si no hay sesión iniciada (null), el operador Elvis (?:) corta la función con el return.
        val uidUsuario = FirebaseAuth.getInstance().currentUser?.uid
            ?: return

        // DEFINIR RUTA (Referencia): Creamos la "dirección" exacta del documento.
        // Usamos el ID del episodio como nombre del documento para que sea único y fácil de encontrar/borrar.
        //La variable ref: Solo define la dirección. No crea nada en la nube por sí sola.
        // El método .set(): Es el que construye la ruta. Si las colecciones o documentos no existen,
        //  Firebase los crea automáticamente al recibir los datos.
        val ref = firestore
            .collection("usuarios")           // Carpeta principal de usuarios
            .document(uidUsuario)             // Documento del usuario actual
            .collection("episodios_vistos")   // Subcolección de su historial
            .document(ep.id.toString())       // El archivo específico del episodio

        // LÓGICA DE ACTUALIZACIÓN:
        // Aquí decidimos si guardamos información o la eliminamos de la nube.
        if (visto) {
            // Si el switch se activó (visto == true), preparamos los datos para guardar.
            //El hashMapOf: Es el contenido del paquete.
            // Firestore: Lo guarda como campos y valores (parecido a un JSON, pero más organizado).
            val datos = hashMapOf(
                "name" to ep.name,
                "episode" to ep.episode,
                "air_date" to ep.air_date,

            )

            ref.set(datos)      // marcar como visto
        } else {
            ref.delete()        // desmarcar → borrar
        }
    }



fun cargarPersonajes() {

    //===========================================
    // BLOQUE CONFIGURACIÓN DE RETROFIT
    // Creamos el "servicio" a partir de la interfaz ApiService
    val api = RetrofitClient.instance.create(ApiService::class.java)

    //extraigo la lista de urls qeu contiene le atributo character
    val listaUrlsPersonajes = episodio?.characters


    //Usamos el signo '?' porque 'listaUrlsPersonajes' podría ser nula.
    //'.map' recorre cada URL de la lista (una por una).
    //'substringAfterLast("/")' busca la última barra inclinada "/"
    // y se queda con todo lo que hay despues y lo guarda en una lista
    val idsList = listaUrlsPersonajes?.map { url ->
        url.substringAfterLast("/")
    }

    //“Si es null, salgo de la función.”
    var ids = idsList?.joinToString(",") ?: return


    api.getPersonajes(ids).enqueue(object : Callback<List<Personaje>> {

        // -----------------------------------------
        // RESPUESTA CORRECTA DEL SERVIDOR
        // -----------------------------------------

        override fun onResponse(
            call: Call<List<Personaje>>,
            response: Response<List<Personaje>>
        ) {
            //Verifica si la respuesta del servidor fue exitosa (código 200 OK).
            if (response.isSuccessful) {

                // response.body()' contiene los datos reales (la lista de personajes).
                // Usamos '.let { ... }' para ejecutar el código solo si los datos NO son nulos.
                response.body()?.let { datosRecibidos ->

                    // Se lo enviamos al adaptador para que actualice la lista en la pantalla.
                    personajesAdapter.setData(datosRecibidos)
                }
            }
        }

        override fun onFailure(call: Call<List<Personaje>>, t: Throwable) {
            Toast.makeText(
                requireContext(),
                "Fallo de red: ${t.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    })


}
}

