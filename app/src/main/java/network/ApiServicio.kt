package network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import model.EpisodiosResponse


// 1. Definimos una 'interface'.
// En Retrofit, no escribimos el código de conexión nosotros, solo definimos
// las "reglas" (el contrato) y la librería se encarga de hacer el trabajo sucio.
interface ApiService {

	// 2. La anotación @GET
	// Le dice a Retrofit que queremos RECUPERAR datos (no enviar ni borrar).
	// "episode" es el 'endpoint' o ruta final. Si tu web es "rickandmortyapi.com/api/",
	// esto completará la URL a: "rickandmortyapi.com/api/episode"
	@GET("episode")
	fun getEpisodes(
		// 3. La anotación @Query
		// Esto agrega parámetros de filtro a la URL automáticamente.
		// Al poner "page", la URL final quedará así: .../episode?page=1
		// 'page: Int = 1' significa que si no le pasamos un número, usará el 1 por defecto.
		@Query("page") page: Int = 1
	): Call<EpisodiosResponse>
	// 4. El tipo de retorno Call<...>
	// 'Call' es como una caja o promesa. Significa: "Esta función no devuelve los datos
	// inmediatamente (porque internet es lento), sino que devuelve una 'Llamada' que,
	// cuando termine, contendrá un objeto de tipo EpisodiosResponse".
}