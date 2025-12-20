package com.example.rmviewer.ui.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.rmviewer.R


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
        // --- SECCIÓN: MODO OSCURO ---
        // Buscamos el interruptor (Switch) por la clave "pref_dark_mode" definida en settings.xml
        val switchDark = findPreference<SwitchPreferenceCompat>("pref_dark_mode")

        switchDark?.setOnPreferenceChangeListener { _, newValue ->
            // newValue es el nuevo estado del switch (true o false)
            val esOscuro = newValue as Boolean

            // Aplicamos el tema inmediatamente usando la librería AppCompat
            if (esOscuro) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            true // Retornamos true para que el cambio se guarde en la memoria del móvil
        }

        // --- SECCIÓN: IDIOMA ---
        // Buscamos la lista de idiomas por su clave "pref_language"
        val listLang = findPreference<ListPreference>("pref_language")

        listLang?.setOnPreferenceChangeListener { _, newValue ->
            // newValue será el código del idioma seleccionado (ej: "es" o "en")
            val nuevoIdioma = newValue as String

            // Creamos una lista de idiomas compatible con Android (LocaleListCompat)
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(nuevoIdioma)

            // Esta línea es "mágica": cambia el idioma de TODA la app y la reinicia suavemente
            AppCompatDelegate.setApplicationLocales(appLocale)

            true // Guardamos la elección
        }
        }
    }


