import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import model.EpisodiosResponse

// Esta interfaz define el "contrato" de comunicación con la API.
// Retrofit se encargará de generar automáticamente la implementación real.
// Tú solo declaras qué endpoint quieres llamar y qué tipo de datos esperas.
interface ApiService {

	// @GET("episode") indica que esta función hará una petición HTTP GET.
	// "episode" es la ruta final del endpoint. Si la base es "https://rickandmortyapi.com/api/",
	// la URL completa será: "https://rickandmortyapi.com/api/episode".
	@GET("episode")
	fun getEpisodes(
		// @Query("page") añade un parámetro dinámico a la URL.
		// Ejemplo: si llamas a getEpisodes(2), la URL será:
		// https://rickandmortyapi.com/api/episode?page=2
		//
		// El valor por defecto es 1, así que si no pasas nada,
		// Retrofit usará automáticamente la página 1:
		// https://rickandmortyapi.com/api/episode?page=1
		@Query("page") page: Int = 1
	): Call<EpisodiosResponse>
	// El tipo de retorno es Call<EpisodiosResponse>.
	// "Call" es un contenedor que representa la llamada pendiente.
	// No devuelve los datos inmediatamente, porque la petición HTTP tarda.
	// Cuando la llamada termine, podrás obtener un objeto EpisodiosResponse
	// con los datos reales de la respuesta.
}
