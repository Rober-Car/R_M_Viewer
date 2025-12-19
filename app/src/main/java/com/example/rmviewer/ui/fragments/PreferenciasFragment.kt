package com.example.rmviewer.ui.fragments

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import androidx.preference.PreferenceManager
import com.example.rmviewer.R
import java.util.Locale

class PreferenciasFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        // Carga el XML de preferencias
        setPreferencesFromResource(R.xml.settings, rootKey)

        // Configurar listeners inmediatamente después de inflar las preferencias
        configurarListeners()
    }

    // onResume se ejecuta cuando el Fragmento de Ajustes se vuelve visible para el usuario.
    override fun onResume() {
        super.onResume()
        // Cambiar el título de la Activity cuando se muestra el fragmento.
        requireActivity().title = getString(R.string.title_ajustes)
    }

    private fun configurarListeners() {
        // Listener para modo oscuro (SwitchPreferenceCompat)
        val switchDark = findPreference<SwitchPreferenceCompat>("pref_dark_mode")
        switchDark?.setOnPreferenceChangeListener { _, newValue ->
            // newValue es Boolean; la librería guarda el valor automáticamente.
            // Forzamos recreación para que MainActivity vuelva a leer la preferencia y aplique el tema.
            requireActivity().recreate()
            true // devolver true para que la preferencia se guarde
        }

        // Listener para idioma (ListPreference)
        val listLang = findPreference<ListPreference>("pref_language")
        listLang?.setOnPreferenceChangeListener { pref, newValue ->
            // newValue es el código de idioma ("es" / "en").
            val nuevoIdioma = (newValue as? String) ?: "es"

            // Guardado automático por la librería PreferenceFragmentCompat.
            // Forzamos la recreación para que MainActivity (attachBaseContext) lea la nueva preferencia.
            requireActivity().recreate()
            true
        }
    }

    // Método auxiliar para cambiar idioma in-situ si lo necesitas (no obligatorio aquí,
    // porque usamos recreate() y MainActivity aplica el locale en attachBaseContext).
    private fun aplicarIdiomaLocalmente(idioma: String) {
        val locale = Locale(idioma)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        requireActivity().resources.updateConfiguration(config, requireActivity().resources.displayMetrics)
    }
}
