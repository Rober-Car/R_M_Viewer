package com.example.rmviewer.ui.fragments

import android.os.Bundle
import androidx.preference.Preference

import androidx.preference.PreferenceFragmentCompat
import com.example.rmviewer.R

class PreferenciasFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    // onResume se ejecuta cuando el Fragmento de Ajustes se vuelve visible para el usuario.
    override fun onResume() {
        // LLAMADA BASE: Ejecuta las tareas necesarias de la superclase.
        super.onResume()

        // CAMBIO DE TÍTULO: Accede a la Actividad que contiene este Fragmento
        // y cambia el texto de la barra superior a "AJUSTES".
        requireActivity().title = getString(R.string.title_ajustes)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        // FILTRO: Comprueba si la preferencia que se acaba de tocar
        // tiene la clave exacta "pref_dark_mode".
        if (preference.key == "pref_dark_mode") {

            //RECREACIÓN: Obliga a la actividad actual a cerrarse y volver a abrirse.
            // Esto es necesario para que Android vuelva a leer los recursos (colores)
            // y aplique el tema oscuro o claro de inmediato sin reiniciar la app.
            requireActivity().recreate()
        }

        //CONTINUIDAD: Devuelve el comportamiento normal de la clase padre
        // para que el clic se registre correctamente.
        return super.onPreferenceTreeClick(preference)}



}

