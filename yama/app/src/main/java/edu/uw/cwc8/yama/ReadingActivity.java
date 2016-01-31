package edu.uw.cwc8.yama;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*
* Allow the user to see a list of messages received.
* Display the author, body, and sent date of the message.
 */

public class ReadingActivity extends AppCompatActivity {

    public static final String TAG = "ReadingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
    }
}
