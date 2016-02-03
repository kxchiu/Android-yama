package edu.uw.cwc8.yama;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
* Allow the user to specify the recipient (phone number) and write out a message to send,
* as well as sending it
 */

public class ComposeActivity extends AppCompatActivity {

    public static final String TAG = "ComposeActivity";
    public final int PICK_CONTACT = 1;

    EditText phoneInput;
    EditText messageInput;
    Button contactButton;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        Log.v(TAG, "Using composing activity");

        phoneInput = (EditText)findViewById(R.id.phoneField);
        messageInput = (EditText)findViewById(R.id.messageField);
        contactButton = (Button)findViewById(R.id.btnContact);
        sendButton = (Button)findViewById(R.id.btnSend);

        //sends message when the send button is clicked
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "You sent: \"" + messageInput.getText() + "\" to " + phoneInput.getText());

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneInput.getText().toString(), null, messageInput.getText().toString(), null, null);

                phoneInput.setText("");
                messageInput.setText("");
            }
        });
    }

    //brings up the contact list from the device
    public void viewContact(View view){
        Log.v(TAG, "Viewing contact list");
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent, PICK_CONTACT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT) {
            resultCode = RESULT_OK;
            if (resultCode == RESULT_OK) {
                Uri contact = data.getData();
                String[] numbers = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                Cursor cursor = getContentResolver().query(contact, numbers, null, null, null);
                cursor.moveToFirst();

                int col = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(col);
                cursor.close();

                Log.v(TAG, number);
                phoneInput.setText(number);
            }
        }
    }

}
