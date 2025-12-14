package com.example.rmviewer.model

// Estos son los datos de UN solo episodio.
data class InfoEpisodio(
	val id: Int,
	val name: String,

	// Nota mental: Escribí "air_date" con guion bajo (aunque sea feo en Kotlin)
	// porque en el JSON de la API viene escrito EXACTAMENTE así.
	// Si pongo "airDate" aquí sin avisar, no me traerá el dato.
	val air_date: String,

	val episode: String
)