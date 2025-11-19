package com.example.rmviewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rmviewer.databinding.FragmentEpisodiosBinding


class EpisodiosFragment : Fragment() {


    private lateinit var binding: FragmentEpisodiosBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflamos el layout con bindin, al ser un fragmento esta forma es distinta a las usadas anteriormete
        //Este código se utiliza dentro del método onCreateView de un Fragment.
        /*
        * Los Parámetros del .inflate():

inflater: La herramienta para convertir el XML en objetos View.

container: El ViewGroup padre (perteneciente a la Activity) donde se alojará el diseño del fragmento.

false (attachToRoot): Esto es crucial. Le dice a Android: "Crea la vista usando los parámetros de diseño
*  del contenedor, pero NO la agregues al contenedor todavía"
        * */

        binding= FragmentEpisodiosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.episodiosRecyclerview.layoutManager= LinearLayoutManager(requireContext())

        //adaptador qeu une el componente grafico con las clases kt,

        binding.episodiosRecyclerview.adapter =


    }
}