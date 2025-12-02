package com.example.rmviewer.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.rmviewer.databinding.ActivityMainBinding
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.rmviewer.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    // FirebaseAuth para comprobar sesión del usuario
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Es un estatico qeu devuele un objeto unico (singleton) del servicio de autentificacion de firebase
        // Con esa instancia puedes:
        ///Registrar usuarios (createUserWithEmailAndPassword).
        // Iniciar sesión (signInWithEmailAndPassword).
        // Cerrar sesión (signOut).
        // Consultar el usuario actual (auth.currentUser).
        auth = FirebaseAuth.getInstance()

        // ----------------------------------------------------
        // COMPROBAMOS USUARIO IDENTIFICADO
        // ----------------------------------------------------

        // auth.currentUser devuelve el usuario actualmente autenticado.
        //   Si hay sesión activa devuelve un objeto FirebaseUser.
        //   Si NO hay sesión devuelve null.
        val currentUser = auth.currentUser

        // Verificamos si no hay usuario autenticado
        if (currentUser == null) {
            // si no existe sesión activa en FirebaseAuth.
            // Creamos un Intent para abrir desde la Activity actual hacia la pantalla de login.
            startActivity(Intent(this, LoginActivity::class.java))

            // finish() cierra la Activity actual (MainActivity).Esto evita que
            //  el usuario pueda volver atrás a una pantalla que requiere sesión activa.
            finish()

            // return → detenemos la ejecución del metodo así evitamos seguir cargando el
            // MainActivity cuando no hay usuario logueado.
            return
        }


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // ----------------------------------------------------
        // OBTENCIÓN DE VISTAS
        // ----------------------------------------------------
        val toolbar: Toolbar = binding.toolbar
        drawerLayout = binding.drawerLayout

        // ----------------------------------------------------------------------------------
        // INICIO: BLOQUE DE CONFIGURACIÓN DEL NAVIGATION DRAWER
        // ----------------------------------------------------------------------------------

        // Obtiene el Fragment que actúa como Host para la navegación.
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        // Obtiene el controlador de navegación para cambiar de Fragments.
        val navController = navHostFragment.navController

        // Establece la Toolbar como la barra de acción de la Activity (¡Solo una vez!)
        setSupportActionBar(toolbar)

        // Desactiva la flecha de navegación por defecto que el sistema podría intentar dibujar.
        // Esto asegura que el Toggle tenga el control completo del icono.
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // Crea el objeto ActionBarDrawerToggle .
        // Este objeto sincroniza el icono de hamburguesa con el DrawerLayout.
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        // Asocia el Toggle al DrawerLayout para escuchar eventos y animar el icono.
        drawerLayout.addDrawerListener(toggle)

        // Sincroniza el estado inicial. Esto es lo que dibuja la hamburguesa y prepara la animación.
        toggle.syncState()

        // --------------------------------------------------------------------------------
        // Listener del NavigationView: conecta los ítems del menú lateral con acciones.
        // --------------------------------------------------------------------------------

        // 'item' es el parámetro que recibe la lambda.
        // Representa el MenuItem que el usuario ha pulsado en el NavigationView.
        // Es un objeto de tipo MenuItem, con propiedades como title, icon y itemId.
        binding.navView.setNavigationItemSelectedListener {

            // 'item.itemId' devuelve el identificador único del ítem pulsado.
            // Ese id corresponde al que definiste en tu menú XML (ej. R.id.nav_episodios).
            // Sirve para saber exactamente qué opción del menú se seleccionó.
                item ->
            when (item.itemId) {

                // Cerrar sesión: se desloguea al usuario y se redirige al LoginActivity.
                R.id.nav_logout -> {
                    auth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }

                R.id.nav_episodios -> {
                    navController.navigate(R.id.episodiosFragment)
                    drawerLayout.close()
                    true
                }

                R.id.nav_ajustes -> {
                    navController.navigate(R.id.ajustesFragment)
                    drawerLayout.close()
                    true
                }

                R.id.nav_Estadisticas -> {
                    navController.navigate(R.id.estadisticasFragment)
                    drawerLayout.close()
                    true
                }

                R.id.nav_acerca_de -> {
                    navController.navigate(R.id.acercaFragment)
                    drawerLayout.close()
                    true
                }

                else -> false
            }

        }

    }


}
