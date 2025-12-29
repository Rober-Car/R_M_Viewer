package com.example.rmviewer.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rmviewer.R
import com.example.rmviewer.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class RegisterActivity : AppCompatActivity() {

	private lateinit var binding: ActivityRegisterBinding
	// FirebaseAuth: instancia para crear usuarios
	private lateinit var auth: FirebaseAuth

	// Longitud mínima recomendada para la contraseña
	private val MIN_PASSWORD_LENGTH = 6

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityRegisterBinding.inflate(layoutInflater)
		setContentView(binding.root)


		// Inicializamos FirebaseAuth
		//Devuelve un objeto FirebaseAuth que representa la conexión con el servicio de autenticación de Firebase.
		// Esa instancia es única (singleton) dentro de la aplicación: siempre que lo llames, obtendrás la misma referencia.
		//A partir de ahí, puedes usar auth para realizar operaciones como:
		// Registrar usuarios con email y contraseña.
		// Iniciar sesión, Cerrar sesión y Consultar el usuario actualmente autenticado (auth.getCurrentUser()).
		auth = FirebaseAuth.getInstance()

		// Volver a Login
		binding.btnVolverRegistro.setOnClickListener {
			startActivity(Intent(this, LoginActivity::class.java))
			finish()
		}

		// Click en registrar
		binding.btnRegistro.setOnClickListener {
			// Llama a la fucnion combinadora que valida los campos
			if (validarRegistro()) {
				// Si pasa validaciones, procedemos a registrar en Firebase
				registrarEnFirebase()
			}
		}
	}

	//Función privada que gestiona el registro de un nuevo usuario en Firebase Authentication
	private fun registrarEnFirebase() {
		// Obtenemos el texto introducido en el campo de email y lo limpiamos de espacios
		val email = binding.edtEmailRegistro.text.toString().trim()
		// Obtenemos el texto introducido en el campo de contraseña y lo limpiamos de espacios
		val password = binding.edtPasswordRegistro.text.toString().trim()
		// Obtenemos el texto introducido en el campo de nombre y lo limpiamos de espacios
		val nombre = binding.edtNombreRegistro.text.toString().trim()

		// Desactivamos el botón de registro para evitar que el usuario lo pulse varias veces
		binding.btnRegistro.isEnabled = false



		// Crea un nuevo usuario  de Firebase usando un correo electrónico y una contraseña.
		// Envía la petición a los servidores de Firebase para registrar un nuevo usuario con esas credenciales.
		// Si el correo ya existe en la base de datos de Firebase, devuelve un error (FirebaseAuthUserCollisionException).
		// Si el correo y la contraseña cumplen las reglas, crea el usuario.
		// Al terminar, devuelve una Task<AuthResult>, que es una tarea asíncrona
		auth.createUserWithEmailAndPassword(email, password)
			// addOnCompleteListener se ejecuta cuando la tarea termina (éxito o error)
			.addOnCompleteListener(this) { task ->
				if (task.isSuccessful) {
					// Si el registro fue exitoso, Firebase autentica automáticamente al usuario
					// obtenemos el identificador. auth.currentUser devuelve el usuario actual, y .uid es su identificador único
					val uid = auth.currentUser?.uid

					// Mostramos un mensaje confirmando que el registro fue correcto
					Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()

					// Creamos un Intent para abrir la pantalla principal (MainActivity)
					val intent = Intent(this, LoginActivity::class.java)
					startActivity(intent)

					// Cerramos RegisterActivity para que no quede en el historial
					// y el usuario no pueda volver atrás con el botón "atrás"
					finish()

				} else {
					// Si el registro falla, reactivamos el botón para que el usuario pueda intentarlo de nuevo
					binding.btnRegistro.isEnabled = true

					// Obtenemos la excepción que describe el error
					val exception = task.exception
					when (exception) {
							// Caso específico: el email ya está registrado en Firebase
						// FirebaseAuthUserCollisionException → se lanza cuando intentas crear un usuario
						// con un correo que ya existe en la base de datos de Firebase Authentication.
						is FirebaseAuthUserCollisionException -> {
							// Marcamos el campo de email con un error visual (aparece un mensaje debajo del EditText)
							binding.edtEmailRegistro.error = "Este correo ya está registrado"

							// Ponemos el foco en el campo de email para que el usuario lo corrija
							binding.edtEmailRegistro.requestFocus()

							// Mostramos un Toast explicando que el email ya está en uso
							Toast.makeText(this, "Este correo ya está registrado", Toast.LENGTH_LONG).show()
						}

						// Caso genérico: cualquier otra excepción distinta a "email ya en uso"
						else -> {
							// Mostramos un Toast con el mensaje de error que devuelve Firebase
							// Ejemplos: "La contraseña debe tener al menos 6 caracteres", "Formato de email inválido"
							Toast.makeText(this, "Error: ${exception?.message}", Toast.LENGTH_LONG).show()
						}
					}
				}
			}

	}
	// =================== VALIDADORES INDIVIDUALES ===================


	// funcion que evalua el nombre

	private fun validaNombre(): Boolean {
		val nombre = binding.edtNombreRegistro.text.toString().trim()
		val regex = Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]{2,}$") // mínimo 2 letras
		return if (nombre.isEmpty()) {
			binding.edtNombreRegistro.error = getString(R.string.error_nombre_required)
			false
		} else if (!nombre.matches(regex)) {
			binding.edtNombreRegistro.error = getString(R.string.error_nombre_format)
			false
		} else {
			binding.edtNombreRegistro.error = null
			true
		}
	}

	private fun validaEmail(): Boolean {
		val email = binding.edtEmailRegistro.text.toString().trim()
		val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
		return if (email.isEmpty()) {
			binding.edtEmailRegistro.error = getString(R.string.error_email_required)
			false
		} else if (!regex.matches(email)) {
			binding.edtEmailRegistro.error = getString(R.string.error_email_format)
			false
		} else {
			binding.edtEmailRegistro.error = null
			true
		}
	}

	private fun validaPassword(): Boolean {
		val password = binding.edtPasswordRegistro.text.toString() // ajusta al id real
		return if (password.isEmpty()) {
			binding.edtPasswordRegistro.error = getString(R.string.error_password_required)
			false
		} else if (password.length < 6) {
			binding.edtPasswordRegistro.error = getString(R.string.error_password_length)
			false
		} else {
			binding.edtPasswordRegistro.error = null
			true
		}
	}

	// =================== FUNCION COMBINADORA ===================
	private fun validarRegistro(): Boolean {
		// limpia errores previos (ya hecho en cada validador, pero por si acaso)
		binding.edtNombreRegistro.error = null
		binding.edtEmailRegistro.error = null
		binding.edtPasswordRegistro.error = null

		val nombreOk = validaNombre()
		val emailOk = validaEmail()
		val passOk = validaPassword()

		if (!nombreOk || !emailOk || !passOk) {
			// Opcional: mostrar resumen en Toast
			Toast.makeText(this, getString(R.string.error_fix_fields), Toast.LENGTH_SHORT).show()
			return false
		}
		return true
	}
}
