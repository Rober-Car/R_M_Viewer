package com.example.rmviewer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.rmviewer.databinding.ActivityMainBinding
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout

class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding
	private lateinit var drawerLayout: DrawerLayout

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

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

		// 1. Establece la Toolbar como la barra de acción de la Activity (¡Solo una vez!)
		setSupportActionBar(toolbar)

		// 2. Desactiva la flecha de navegación por defecto que el sistema podría intentar dibujar.
		// Esto asegura que el Toggle tenga el control completo del icono.
		supportActionBar?.setDisplayHomeAsUpEnabled(false)

		// 3. Crea el objeto ActionBarDrawerToggle (el "interruptor" animado).
		val toggle = ActionBarDrawerToggle(
			this,
			drawerLayout,
			toolbar,
			R.string.navigation_drawer_open,
			R.string.navigation_drawer_close
		)

		// 4. Asocia el Toggle al DrawerLayout para escuchar eventos y animar el icono.
		drawerLayout.addDrawerListener(toggle)

		// 5. Sincroniza el estado inicial. Esto es lo que dibuja la hamburguesa y prepara la animación.
		toggle.syncState()

		// --------------------------------------------------------------------------------
		// FIN: BLOQUE DE CONFIGURACIÓN DEL NAVIGATION DRAWER
		// --------------------------------------------------------------------------------

		// Obtiene el Fragment que actúa como Host para la navegación.
		val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
		// Obtiene el controlador de navegación para cambiar de Fragments.
		val navController = navHostFragment.navController
	}
}