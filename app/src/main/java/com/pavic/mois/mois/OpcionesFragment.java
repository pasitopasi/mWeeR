package com.pavic.mois.mois;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Alejandro on 14/11/2017.
 */

public class OpcionesFragment extends PreferenceFragment {

    private Locale locale;
    private Configuration config = new Configuration();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.opciones);

        ListPreference selected_theme = (ListPreference) getPreferenceManager().findPreference("idioma_opcion");
        selected_theme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                startActivity(new Intent(getActivity().getApplicationContext(), InitialActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                getActivity().finish();
                return true;
            }
        });

    }
}
