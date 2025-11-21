package network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 1. Uso 'object' (Singleton).
// Significa que esto es único en toda la app. No necesito crear instancias con 'new'.
// IMPORTANTE: Como es un 'object', se crea sin cosntructor
object RetrofitClient {

	// 2. La URL base. Retrofit necesita una URL base, es decir, el inicio de todas las rutas.
	// Todas empiezan por: https://rickandmortyapi.com/api/
	// Nota mental: Siempre debe terminar en barra '/' o la app fallará.
	// Es 'const' porque nunca va a cambiar.
	private const val BASE_URL = "https://rickandmortyapi.com/api/"

	// 3. La instancia de Retrofit.
	// Uso 'by lazy' Significa: by lazy significa: solo se crea cuando se usa por primera vez y nunca más se vuelve a crear
	// Créalo SOLO la primera vez que yo intente usarlo".
	val instance: Retrofit by lazy {

		// 4. El Constructor (Builder).
		// Le dice a Retrofit: todas las peticiones empiezan desde esta dirección BASE_URL
		Retrofit.Builder()
			.baseUrl(BASE_URL) // Le digo a dónde tiene que llamar.

			// 5. El Traductor (Gson).
			// Sin esto, recibiría texto plano. Esto se encarga de convertir
			// el JSON de internet a mis objetos Kotlin automáticamente.
			.addConverterFactory(GsonConverterFactory.create())

			// 6. ¡Listo! Fabrica el objeto final.
			.build()
	}
}