package com.example.rmviewer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.rmviewer.R
import com.example.rmviewer.databinding.ItemEpisodioBinding
import com.example.rmviewer.databinding.ItemPersonajeBinding
import com.example.rmviewer.model.Episodio
import com.example.rmviewer.model.Personaje

class AdaptadorPersonajes(
    private val onClick: (Personaje) -> Unit
): RecyclerView.Adapter<AdaptadorPersonajes.MyViewHolder>() {

    // --------------------------------------------------------
    // BLOQUE 1: LISTA DE DATOS INTERNA
    // --------------------------------------------------------
    // Usamos una lista mutable para poder actualizarla cuando lleguen datos de la API.

    private val listaPersonajes = mutableListOf<Personaje>()

    // --------------------------------------------------------
    // BLOQUE 2: VIEW HOLDER (gestiona cada tarjeta/ítem del RecyclerView)
    // --------------------------------------------------------
    //class MyViewHolder → es una clase que define cómo se guarda y maneja la vista de un ítem.n
    // es un contenedor de vistas que representa un ítem de la lista.
    //inner class es una clase declarada dentro de otra clase.inner le da
    // a esa clase interna acceso a las propiedades y métodos de la clase externa.
    //Recibe  una instancia del binding del layout que representa cada ítem del RecyclerView.

    inner class MyViewHolder(private val binding: ItemPersonajeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(personaje: Personaje) {

            binding.itemNombrePersonaje.text = personaje.name
            // 1. Prepara a Glide indicándole el "contexto" (la actividad o fragmento).
            Glide.with(binding.imgPersonaje.context)

                //Define QUÉ imagen cargar.
                .load(personaje.image)

                // Define DÓNDE mostrarla.
                // 'into' toma la imageny la coloca automáticamente en el ImageView.
                .into(binding.imgPersonaje)
        }

    }
    // --------------------------------------------------------
    // BLOQUE 3: CREACIÓN DEL VIEW HOLDER
    // --------------------------------------------------------
    ///onCreateViewHolder: Es el encargado de fabricar la "caja" vacía (el diseño XML).
    //Si necesitas 10 celdas para llenar la pantalla, esta función se ejecutará unas 10  veces
    // para construir esas cajas.

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        // Inflamos el layout XML del item
        val binding = ItemPersonajeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        //Metemos el diseño dentro del contenedor (ViewHolder):
        return MyViewHolder(binding)
    }


    // --------------------------------------------------------
    // BLOQUE 4: ASIGNACIÓN DE DATOS A CADA ITEM
    // --------------------------------------------------------

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind((listaPersonajes[position]))
    }

    // --------------------------------------------------------
    // BLOQUE 5: TAMAÑO TOTAL DE ITEMS
    // --------------------------------------------------------
    override fun getItemCount(): Int = listaPersonajes.size


    // --------------------------------------------------------
    // BLOQUE 6: MÉTODO PARA ACTUALIZAR LOS DATOS DEL ADAPTADOR
    // --------------------------------------------------------
    /**
     * Sustituye el contenido del adaptador por una nueva lista.
     * Se llama desde el Fragment cuando llega la respuesta de Retrofit.
     */

    fun setData(newList: List<Personaje>) {
        listaPersonajes.clear()         // Borra los episodios actuales
        listaPersonajes.addAll(newList) // Añade los nuevos
        notifyDataSetChanged()         // Actualiza la vista para ver los cambios
    }
}
