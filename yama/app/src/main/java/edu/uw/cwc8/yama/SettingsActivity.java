package edu.uw.cwc8.yama;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*
* Allow the user to an "auto-reply" message, as well as whether automatic replies should occur.
 */

public class SettingsActivity extends AppCompatActivity {

    public static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, new SettingsFragment())
                .commit();
    }
}
