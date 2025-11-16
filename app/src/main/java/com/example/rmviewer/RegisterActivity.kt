package com.example.rmviewer

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rmviewer.databinding.ActivityLoginBinding
import com.example.rmviewer.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

	private lateinit var binding: ActivityRegisterBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

		binding= ActivityRegisterBinding.inflate(layoutInflater)
		setContentView(binding.root)
		setContentView(R.layout.activity_register)

		//Vuelvo a la pantalla de login al pulsar el btn volver a login

		binding.btnVolverRegistro.setOnClickListener{

			val intent= Intent(this, LoginActivity::class.java)
			startActivity(intent)

		}


		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}
	}
}