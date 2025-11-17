package com.example.rmviewer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rmviewer.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

	private lateinit var binding: ActivityRegisterBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)


		binding = ActivityRegisterBinding.inflate(layoutInflater)
		setContentView(binding.root)

		// Volver a Login
		binding.btnVolverRegistro.setOnClickListener {
			startActivity(Intent(this, LoginActivity::class.java))
			finish()
		}

		// Click en registrar
		binding.btnRegistro.setOnClickListener {
			// Llama AL COMBINADOR que ya lee los campos y muestra errores en los EditText
			if (validarRegistro()) {
				// TODO: aquí llamarás a FirebaseAuth.createUserWithEmailAndPassword(...)

				binding.btnRegistro.isEnabled = false
				Toast.makeText(this, "Registro OK (simulado)", Toast.LENGTH_SHORT).show()
				startActivity(Intent(this, MainActivity::class.java))
				finish()
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
