package com.example.rmviewer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rmviewer.databinding.EpisodioItemBinding

// CLASE ADAPTADOR: Es el "camarero".
// Recibe dos cosas:
// 1. La comida (listaEpisodios).
// 2. Las instrucciones de qué hacer si el cliente toca un plato (onClick).
class AdatadorEpisodios(
    private val listaEpisodios: List<Episodio>,
    private val onClick: (Episodio) -> Unit
) : RecyclerView.Adapter<AdatadorEpisodios.MyViewHolder>() {

    // CLASE VIEWHOLDER: Es la "bandeja" o "cajita".
    // Guarda las referencias al diseño (binding) para no tener que buscarlas todo el tiempo.
    // 'inner' significa que esta clase puede ver las variables de la clase padre (AdatadorEpisodios).
    inner class MyViewHolder(private val binding: EpisodioItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // FUNCION BIN (BIND): El momento de "servir".
        // Coge los datos de un episodio y rellena los huecos del XML.
        fun bin(episodio: Episodio) {
            // 1. Ponemos el texto
            binding.nombreEpisodio.text = episodio.name

            // 2. Lógica simple para cambiar la imagen según el nombre
            when (episodio.name) {
                "Episodio 1" -> binding.imagenItem.setImageResource(R.drawable.hommer)
                "Episodio 2" -> binding.imagenItem.setImageResource(R.drawable.fantasma)
            }

            // 3. EL CLICK:
            // 'root' es toda la tarjeta/fila.
            // Al tocarla, ejecutamos la función 'onClick' enviando este episodio concreto.
            binding.root.setOnClickListener {
                onClick(episodio)
            }
        }
    }

    // ON CREATE VIEW HOLDER: Se ejecuta al principio solo unas pocas veces.
    // "Infla" (crea) el diseño XML de UN solo ítem (la tarjeta vacía) y lo mete en el ViewHolder.
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdatadorEpisodios.MyViewHolder {
        val binding =
            EpisodioItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    // ON BIND VIEW HOLDER: Se ejecuta CONSTANTEMENTE al hacer scroll.
    // "Recicla" la vista. Llama a nuestra función 'bin' para pintar los datos nuevos
    // en una tarjeta que se acaba de volver visible.
    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.bin(listaEpisodios[position])
    }


    // GET ITEM COUNT: Información básica.
    // Le dice al sistema cuántos elementos hay en total para calcular el tamaño de la barra de scroll.
    override fun getItemCount(): Int {
        return listaEpisodios.size
    }
}