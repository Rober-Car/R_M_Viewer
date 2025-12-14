package com.example.rmviewer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rmviewer.databinding.EpisodioItemBinding
import com.example.rmviewer.model.Episodio

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
    //class MyViewHolder → es una clase que define cómo se guarda y maneja la vista de un ítem.n
    // es un contenedor de vistas que representa un ítem de la lista.
    //inner class es una clase declarada dentro de otra clase.La palabra clave inner le da
    // a esa clase interna acceso a las propiedades y métodos de la clase externa.
    inner class MyViewHolder(private val binding: EpisodioItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //bind es una función personalizada se encarga de recibir un objeto de datos (como un Episodio,
        // Post, etc.) Y rellenar las vistas del layout del ítem con esos datos.
        fun bind(episodio: Episodio) {

            // Muestra los textos del item
            binding.itemNombreEpisodio.text = episodio.name
            binding.itemCodigoEpisodio.text = episodio.episode  //fecha
            binding.itemFechaEpisodio.text = episodio.air_date //codigo

            // --------------------------------------------------------
            // CONTROL DE ICONO "VISTO"
            // --------------------------------------------------------

            //.visibility es una propiedad de cualquier View,controla cómo se muestra u oculta esa
            // vista en la interfaz.

            // Cambia la visibilidad de esa vista. Puede ser:
            //View.VISIBLE → se muestra.
            // View.INVISIBLE → no se ve, pero ocupa espacio.
            // View.GONE → no se ve ni ocupa espacio.
            binding.iconVisto.visibility =
                    // android.view.View es la superclase de todos los componentes visuales en Android.
                if (episodio.visto) android.view.View.VISIBLE
                else android.view.View.INVISIBLE


            // --------------------------------------------------------
            // BLOQUE 2.2: CLICK EN EL ITEM
            // --------------------------------------------------------
            binding.root.setOnClickListener {
                onClick(episodio)
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
