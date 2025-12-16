package com.example.rmviewer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rmviewer.databinding.FragmentDetallesBinding
import com.example.rmviewer.model.Episodio
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth


class DetallesFragment : Fragment() {

    private lateinit var binding: FragmentDetallesBinding

    // Objeto recibido del fragment anterior
    private var episodio: Episodio? = null

    //Crea un objeto (database) para gestionar la Base de Datos (guardar/leer datos).
    // Conéctata la base de datos de Firebase de ESTE proyecto
    private val database = FirebaseDatabase.getInstance()

    // Crea una referencia que apunta al nodo principal "Usuarios" en la base de datos de Firebase.
    // Si no existe, Firebase la creará cuando escribas algo
    private val usuariosRef = database.getReference("Usuarios")


    //  Inicializa la conexión con el servicio de Autenticación de Firebase
    private val auth = FirebaseAuth.getInstance()

    // Obtiene el UID (identificador único) del usuario que ha iniciado sesión actualmente.
    //Se usa '!!' porque se asume que el usuario SIEMPRE está logueado en este punto y no puede ser null
    private val uid = auth.currentUser!!.uid



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

            // ? Le dice al compilador: "Solo continúa con lo que sigue si el objeto  no es null
            //let es una de las funciones de ámbito
            // Función: Ejecuta el bloque de código definido dentro de sus llaves ({ ... })
            // solo si el operador ?. permitió la llamada (es decir, si episodio no era null)
            // usando la referencia segura ep, asigna el valor del Switch (isChecked)a la propiedad .visto.
            episodio?.let { ep ->
                ep.visto = isChecked

                // Obtenemos el UID del usuario (ya está logueado)
                //Elvis Operator, si el valor de la izquierda es nulo, usa el valor de la derecha
                //return@setOnCheckedChangeListener Detiene la ejecución del bloque de código  y sale de él inmediatamente.
                val uidUsuario = FirebaseAuth.getInstance().currentUser?.uid
                    ?: return@setOnCheckedChangeListener

                // Toma el valor de la propiedad 'id' del objeto 'ep'.y lo Convierte en una cadena de texto
                // Almacena el resultado final (el ID como String) en la nueva variable 'id'.
                val idEpisodio = ep.id.toString()

                //  Construye la ruta de la base de datos (Referencia):
                //child() 👉 entra o crea carpetas
                // setValue() 👉 guarda el contenido dentro
                val referencia = database // Comienza en la raíz de la Base de Datos.
                    .getReference("usuarios") ///usuarios,Si no existe, Firebase la creará
                    .child(uidUsuario)
                    .child("episodios_vistos")
                    .child(idEpisodio)

                //referencia es un DatabaseReference que apunta exactamente a un episodio.           /
                // Guarda el valor 'isChecked' (true o false) en la ubicación final de la referencia.
                // Firebase escribe el valor en ese nodo
                referencia.setValue(isChecked)

            }
        }
    }
}

