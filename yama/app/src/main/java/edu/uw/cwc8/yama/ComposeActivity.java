package edu.uw.cwc8.yama;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*
* Allow the user to specify the recipient (phone number) and write out a message to send,
* as well as sending it
 */

public class ComposeActivity extends AppCompatActivity {

    public static final String TAG = "ComposeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
    }
}
