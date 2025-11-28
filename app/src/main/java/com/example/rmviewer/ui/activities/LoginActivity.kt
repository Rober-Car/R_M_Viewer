package com.example.rmviewer.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rmviewer.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

	private lateinit var binding: ActivityLoginBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)


		binding= ActivityLoginBinding.inflate(layoutInflater)
		setContentView(binding.root)

		//abro el actyvity de registro al pulsar registrase
		binding.txtRegisterLogin.setOnClickListener{
			val intent = Intent(this, RegisterActivity::class.java)
			startActivity(intent)

		}


	}


	// =================== VALDIAR DATOS ===================



// fucnion que valida si los datos estan en la base de datos, deberia validar primero si estan bien escritos?
	private fun validarLogin(){
		val email = binding.edtEmailLogin.text.toString()
		val password = binding.edtPasswordLogin.text.toString()

	}
}