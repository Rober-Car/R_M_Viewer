package model

/**
 * Data class usada en la UI y en el adaptador.
 * La estructura incluye los campos que devuelve la API de Rick & Morty,
 * para que Retrofit/Gson pueda mapear directamente el JSON a esta clase.
 *
 * Además añadimos `viewed` (campo local, por defecto false) para marcar
 * si el usuario ha visto el episodio (se persistirá en Firestore más adelante).
 */
data class Episodio(
	val id: Int,                    // id del episodio (ej: 1)
	val name: String,               // nombre del episodio (ej: "Pilot")
	val air_date: String,           // fecha de emisión tal como viene en JSON
	val episode: String,            // código SxxExx (ej: "S01E01")
	val characters: List<String>,   // lista de URLs de personajes (se puede usar luego)
	val url: String?,               // URL del recurso en la API (opcional)
	val created: String?,           // timestamp de creación en la API (opcional)

	// ----------------------------
	// Campo local (no proviene de la API)
	// ----------------------------
	var viewed: Boolean = false     // indica si el usuario lo marcó como visto
)