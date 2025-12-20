package com.example.rmviewer.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import com.example.rmviewer.R
import com.example.rmviewer.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    // FirebaseAuth para comprobar sesión del usuario
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ----------------------------------------------------
        // AUTENTICACIÓN
        // ----------------------------------------------------

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

        // ----------------------------------------------------
        // TEMA OSCURO / CLARO
        // ----------------------------------------------------

        // ACCESO A PREFERENCIAS: Abrimos el "archivo" de configuración de la app.
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        // LECTURA: Buscamos el valor guardado con la clave "pref_dark_mode".
        val darkModeEnabled = prefs.getBoolean("pref_dark_mode", false)

        // APLICACIÓN DEL TEMA:
        if (darkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // ----------------------------------------------------
        // VIEW BINDING inflar vistas
        // ----------------------------------------------------
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ----------------------------------------------------
        // CONFIGURAR TOOLBAR Y NAVEGACIÓN
        // ----------------------------------------------------
        val toolbar = binding.toolbar
        drawerLayout = binding.drawerLayout

        // ----------------------------------------------------
        // NAVIGATION DRAWER
        // ----------------------------------------------------
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // ----------------------------------------------------
        // MENÚ LATERAL
        // ----------------------------------------------------
        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_logout -> {
                    auth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }

                R.id.nav_episodios -> {
                    navController.popBackStack(R.id.episodiosFragment, false)
                    drawerLayout.close()
                    true
                }

                R.id.nav_ajustes -> {
                    navController.navigate(R.id.preferenciasFragment)
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
