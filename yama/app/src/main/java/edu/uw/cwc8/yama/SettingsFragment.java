package edu.uw.cwc8.yama;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;

/*
* Fragment for the Settings Activity. Allows the user to customize auto-reply options and messages.
 */

public class SettingsFragment extends PreferenceFragment {

    private static final String TAG = "SettingFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v(TAG, "Starting settings fragment");

        addPreferencesFromResource(R.xml.preferences);

        SwitchPreference autoReply = (SwitchPreference) findPreference("autoReply");

        //allow access to the application's preference settings
        final SharedPreferences sharedPref = getActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);

        //update the preference setting on preference change
        if (autoReply != null){
            Log.v(TAG, "Setting auto reply");
            autoReply.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference arg0, Object switchAuto) {
                    //sets whether the auto-reply setting is on or off
                    boolean isOn = ((Boolean) switchAuto).booleanValue();

                    //save the changes
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("autoReply", isOn).commit();
                    Log.v(TAG, "" + isOn);
                    return true;
                }
            });
        }

        //allow the user to add or update the auto-reply message
        EditTextPreference message = (EditTextPreference) findPreference("autoReplyMessage");
        if(message != null){
            Log.v(TAG, "Setting auto reply message");
            message.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                @Override
                public boolean onPreferenceChange(Preference arg0, Object switchMsg) {
                    //add or change the customized message for auto-reply
                    String myMsg = switchMsg.toString();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("autoReplyMessage", myMsg).commit();

                    Log.v(TAG, myMsg);
                    return true;
                }
            });
        }
    }
}