package com.example.rmviewer.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rmviewer.R
import com.example.rmviewer.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    // ViewBinding para acceder a los elementos del layout sin findViewById
    private lateinit var binding: ActivityLoginBinding

    // Objeto de FirebaseAuth para manejar login
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializamos FirebaseAuth
        //Devuelve un objeto FirebaseAuth que representa la conexión con el servicio de autenticación de Firebase.
        // Esa instancia es única (singleton) dentro de tu aplicación: siempre que lo llames, obtendrás la misma referencia.
        //A partir de ahí, puedes usar auth para realizar operaciones como:
        // Registrar usuarios con email y contraseña.

        // Iniciar sesión, Cerrar sesión y Consultar el usuario actualmente autenticado (auth.getCurrentUser()).
        auth = FirebaseAuth.getInstance()

        // ==========================
        // BOTÓN DE LOGIN
        // ==========================
        binding.btnLogin.setOnClickListener {

            validarLogin()
        }

        // ==========================
        // TEXTO  CLICABLE "Registrarse"
        // ======================

        binding.txtRegisterLogin.setOnClickListener {
            // Abrir RegisterActivity al pulsar "Registrarse"
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }


    // =================== VALDIAR DATOS ===================


    // fucnion que valida si los datos estan en la base de datos
    private fun validarLogin() {
        // Obtener email y contraseña desde los EditText
        val email = binding.edtEmailLogin.text.toString()
        val password = binding.edtPasswordLogin.text.toString()

        // 1️⃣ Campos vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.msg_campos_vacios), Toast.LENGTH_SHORT).show()
            return
        }

        // Patterns.EMAIL_ADDRESS → patrón de regex para validar emails.
        // matcher(email) → compara el texto 'email' con ese patrón.
        // matches() → devuelve true si el email es válido, false si no.
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            //Si el email no es valido, mostramos un mensaje corto en pantalla avisando al usuario
            Toast.makeText(this,getString(R.string.msg_email_invalido) , Toast.LENGTH_SHORT).show()

            // Con 'return' detenemos la ejecución del método actual.
            // Así evitamos seguir con el proceso
            // usando un email incorrecto.
            return
        }

        // Iniciamos sesión en Firebase con email y contraseña proporcionados por el usuario.
        auth.signInWithEmailAndPassword(email, password)

            // addOnCompleteListener(...) → añade un "escuchador" que se dispara cuando la Task termina.
            // Se ejecuta tanto si la autenticación fue exitosa como si falló.
            .addOnCompleteListener(this) { task ->

                // task.isSuccessful → devuelve true si la autenticación fue correcta.
                // Internamente significa que Firebase validó el email y la contraseña contra su base de datos.
                if (task.isSuccessful) {

                    // auth.currentUser → devuelve el usuario actualmente autenticado en Firebase.
                    // ?.uid → accede al identificador único del usuario (UID) si existe.
                    // Este UID es la clave primaria que Firebase usa para identificar al usuario en la base de datos.
                    val uid = auth.currentUser?.uid

                    //Mensaje de confiramcion
                    Toast.makeText(this, getString(R.string.inicio_correcto), Toast.LENGTH_SHORT).show()

                    // Intent(...) → objeto que describe una acción: abrir otra Activity.
                    // startActivity(intent) → lanza la nueva Activity (MainActivity).
                    //MainActivity::class.java → clase de la Activity destino.
                    startActivity(Intent(this, MainActivity::class.java))

                    // finish() → cierra la Activity actual (LoginActivity).Evita que el usuario pueda volver
                    // atrás después de haber iniciado sesión.
                    finish()

                } else {
                    //   mensaje de error (ej: "La contraseña es incorrecta").
                    Toast.makeText(this, getString(R.string.credenciales_incorrectas), Toast.LENGTH_SHORT)
                        .show()
                }
            }


    }
}