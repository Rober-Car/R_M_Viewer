package com.example.rmviewer.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.rmviewer.R
import com.example.rmviewer.databinding.FragmentEstadisticasBinding
import com.example.rmviewer.model.EpisodiosResponse
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import network.ApiService
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EstadisticasFragment : Fragment() {


    private lateinit var binding: FragmentEstadisticasBinding

    // Creamos el "servicio" a partir de la interfaz ApiService
    val api = RetrofitClient.instance.create(ApiService::class.java)

    //Crea un objeto (database) para gestionar la Base de Datos (guardar/leer datos).
    // Conéctata la base de datos de Firebase de ESTE proyecto
    private val database = FirebaseDatabase.getInstance()

    private val firestore = FirebaseFirestore.getInstance()

    private var totalEpisodios: Int = 0

    private var vistos: Int = 0


    //Es la función más importante. Su único trabajo es "inflar" el diseño XML
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEstadisticasBinding.inflate(inflater, container, false)
        return binding.root
    }




    //(El Cerebro)
    //Esta es la función donde ocurre la magia. Se ejecuta justo después de que la vista se ha creado.
    // Es el lugar más seguro para configurar botones, listas y observadores.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Titulo del toolbar
        requireActivity().title = getString(R.string.title_estadisticas)

        contadorEpisodios()
        contadorEpisodiosVistos()

    }

    private fun contadorEpisodios() {

        api.getEpisodios(1).enqueue(object : Callback<EpisodiosResponse> {

            // -----------------------------------------
            // RESPUESTA CORRECTA DEL SERVIDOR
            // -----------------------------------------
            //El método onResponse sirve para recibir y procesar el paquete de información que viene desde internet
            //Es obligatorio Cuando usas llamadas Asíncronas (.enqueue)
            override fun onResponse(
                //El Call es la orden que le diste a Retrofit: "Tu misión es ir a esta URL y traerme episodios"
                call: Call<EpisodiosResponse>,
                //response : lo que hay dentro de él es un objeto de tipo Response,
                //response es el nombre que le das al hueco donde Retrofit va a depositar la información
                response: Response<EpisodiosResponse>
            ) {
                //  Verificamos dos cosas:
                // - 'isSuccessful': Que el código sea de éxito y no un error
                // - 'body() != null': Que el contenido del paquete no venga vacío.
                if (response.isSuccessful && response.body() != null) {

                    // Guardamos el número total de episodios que hay en la API.
                    // Accedemos a 'body()', luego al objeto 'info' y finalmente al campo 'count'.
                    // El '!!' se usa porque arriba ya comprobamos que no es nulo, así que es seguro.
                    totalEpisodios = response.body()!!.info.count
                    actualizarUI()
                }
            }

            override fun onFailure(call: Call<EpisodiosResponse>, t: Throwable) {
                // De momento no hacemos nada
            }

        })
    }

    private fun contadorEpisodiosVistos() {

        //  Si el usuario no está conectado, no podemos saber qué ha visto.
        // Salimos de la función con 'return' para evitar errores.
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        //Ruta en Firestore: Navegamos por la estructura de carpetas (colecciones).
        firestore
            .collection("usuarios")           // Entra en la carpeta de usuarios
            .document(uid)                    // Busca el documento con el ID del usuario
            .collection("episodios_vistos")   // Entra en su lista personal de vistos
            .get()
            //La petición (.get()): Es el acto de pedir la información.
            //  RESPUESTA EXITOSA: Este bloque solo se ejecuta cuando los datos llegan de Google.
            .addOnSuccessListener { snapshot ->  // Se ejecuta cuando Firestore responde con éxito

              // snapshot.size nos dice cuántos documentos hay en esa carpeta.
                vistos  = snapshot.size()
                //Una vez que tenemos el número, llamamos a la función que pinta la pantalla
                actualizarUI()

            }
    }

    //funcion que muestra en el layout el resultado de los calculos
    private fun actualizarUI() {

        // Si aún no han llegado ambos datos, no pintamos nada
        if (totalEpisodios == 0) return

        //envio datos
        binding.tvEpisodiosVistos.text = getString(R.string.stats_vistos, vistos)
        binding.tvTotalEpisodios.text =getString(R.string.stats_total, totalEpisodios)


        if (totalEpisodios > 0) {
            val porcentaje = (vistos * 100) / totalEpisodios
            binding.tvPorcentaje.text = getString(R.string.stats_porcentaje, porcentaje)

            //Llamo a la fucnion que pinta el grafico
            pintarGrafico()
        }
    }

    private fun pintarGrafico() {

        val pieChart = binding.pieChart



        // isDrawHoleEnabled = false: Convierte el gráfico en un círculo sólido (sin el agujero de "donut").
        pieChart.isDrawHoleEnabled = false

        // setUsePercentValues(true): Convierte los valores numéricos automáticamente a porcentajes (%).
        pieChart.setUsePercentValues(true)

        // description.isEnabled = false: Elimina el texto pequeño ("Description Label") de la esquina.
        pieChart.description.isEnabled = false

        // legend.isEnabled = false: Oculta el cuadro de leyendas (cuadritos de colores con texto).
        pieChart.legend.isEnabled = false

        // Configura el color y tamaño de los nombres ("Vistos"/"No vistos") dentro de las rebanadas.
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setEntryLabelTextSize(14f)

        // --- PREPARACIÓN DE DATOS ---
        // Calculamos los dos sectores: los episodios ya vistos y la resta para los restantes.
        val entries = listOf(
            PieEntry(vistos.toFloat(), getString(R.string.chart_label_vistos)),
            PieEntry((totalEpisodios - vistos).toFloat(), getString(R.string.chart_label_no_vistos))
        )

        // .apply nos permite configurar el objeto dataSet de forma limpia sin repetir su nombre.
        val dataSet = PieDataSet(entries, "").apply {
            // Asignamos colores específicos a cada sector desde tu archivo colors.xml.
            colors = listOf(
                ContextCompat.getColor(requireContext(), R.color.azul_rm),
                ContextCompat.getColor(requireContext(), R.color.amarillo)
            )
            // Estilo de los números porcentuales que se pintan sobre el gráfico.
            valueTextSize = 16f
            valueTextColor = Color.BLACK
        }

        // --- APLICACIÓN FINAL ---
        // Unimos los datos configurados al componente de la vista.
        pieChart.data = PieData(dataSet)

        // .invalidate(): Función vital. Fuerza al gráfico a redibujarse para mostrar los cambios.
        pieChart.invalidate()
    }
}