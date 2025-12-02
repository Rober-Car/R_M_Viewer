package com.example.rmviewer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rmviewer.R
import com.example.rmviewer.databinding.EpisodioItemBinding
import model.Episodio

/**
 * Adaptador para mostrar episodios en un RecyclerView.
 * Recibe un callback 'onClick' para manejar clicks en cada item.
 */
class AdaptadorEpisodios(
    private val onClick: (Episodio) -> Unit   // Callback que se ejecuta al pulsar un episodio
) : RecyclerView.Adapter<AdaptadorEpisodios.MyViewHolder>() {

    // --------------------------------------------------------
    // BLOQUE 1: LISTA DE DATOS INTERNA
    // --------------------------------------------------------
    // Usamos una lista mutable para poder actualizarla cuando lleguen datos de la API.
    private val listaEpisodios = mutableListOf<Episodio>()


    // --------------------------------------------------------
    // BLOQUE 2: VIEW HOLDER (gestiona cada tarjeta/ítem del RecyclerView)
    // --------------------------------------------------------
    inner class MyViewHolder(private val binding: EpisodioItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Une un objeto Episodio al layout (item del RecyclerView).
         */
        fun bind(episodio: Episodio) {

            // Muestra el nombre del episodio
            binding.nombreEpisodio.text = episodio.name




            // --------------------------------------------------------
            // BLOQUE 2.2: CLICK EN EL ITEM
            // --------------------------------------------------------
            binding.root.setOnClickListener {
                onClick(episodio)     // Ejecuta el callback que recibimos en el constructor
            }
        }
    }


    // --------------------------------------------------------
    // BLOQUE 3: CREACIÓN DEL VIEW HOLDER
    // --------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Inflamos el layout XML del item
        val binding = EpisodioItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }


    // --------------------------------------------------------
    // BLOQUE 4: ASIGNACIÓN DE DATOS A CADA ITEM
    // --------------------------------------------------------
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listaEpisodios[position])
    }


    // --------------------------------------------------------
    // BLOQUE 5: TAMAÑO TOTAL DE ITEMS
    // --------------------------------------------------------
    override fun getItemCount(): Int = listaEpisodios.size


    // --------------------------------------------------------
    // BLOQUE 6: MÉTODO PARA ACTUALIZAR LOS DATOS DEL ADAPTADOR
    // --------------------------------------------------------
    /**
     * Sustituye el contenido del adaptador por una nueva lista.
     * Se llama desde el Fragment cuando llega la respuesta de Retrofit.
     */
    fun setData(newList: List<Episodio>) {
        listaEpisodios.clear()         // Borra los episodios actuales
        listaEpisodios.addAll(newList) // Añade los nuevos
        notifyDataSetChanged()         // Actualiza la vista para ver los cambios
    }
}
