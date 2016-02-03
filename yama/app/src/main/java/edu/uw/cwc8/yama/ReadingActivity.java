package edu.uw.cwc8.yama;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/*
* Allow the user to see a list of messages received.
* Display the author, body, and sent date of the message.
* Resource used: http://pulse7.net/android/read-sms-message-inbox-sent-draft-android/
 */

public class ReadingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "ReadingActivity";

    private SimpleCursorAdapter adapter;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        Log.v(TAG, "Using reading activity");

        ArrayList<String> list = new ArrayList<String>();
        final Context context = this;

        //initialize an inflator and inflate list_item for content
        LayoutInflater layoutInflater = getLayoutInflater();
        view = layoutInflater.inflate(R.layout.list_item, null);

        //controller
        String[] cols = new String[]{
                Telephony.TextBasedSmsColumns.ADDRESS,
                Telephony.TextBasedSmsColumns.BODY,
                Telephony.TextBasedSmsColumns.DATE
        };
        int[] ids = new int[]{
                view.findViewById(R.id.messageAuthor).getId(),
                view.findViewById(R.id.messageBody).getId(),
                view.findViewById(R.id.messageDate).getId()
        };

        adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item,
                null,
                cols, ids,
                0 //no flag
        );

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == 1) { //if the columnIndex gives the author/sender
                    String author = cursor.getString(columnIndex);
                    Log.v(TAG, "Sent from: " + author);
                    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                            Uri.encode(cursor.getString(columnIndex)));
                    Cursor newCursor = context.getContentResolver()
                            .query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

                    if (newCursor != null && newCursor.moveToNext()) {
                        author = newCursor.getString(0);
                        TextView textView = (TextView) view.findViewById(R.id.messageAuthor);
                        textView.setText("From: " + author);
                    }
                    return true;
                } else if (columnIndex == 2) { //if the columnIndex gives the body of the text message
                    String body = cursor.getString(columnIndex);
                    TextView textView = (TextView) view.findViewById(R.id.messageBody);
                    textView.setText(body);
                    return true;
                } else if (columnIndex == 3) { //if the columnIndex gives the sent date
                    String date = cursor.getString(columnIndex);
                    Long dateLong = Long.parseLong(date);
                    Date dateForm = new Date(dateLong);

                    TextView textView = (TextView) view.findViewById(R.id.messageDate);
                    textView.setText(dateForm.toString());
                    return true;
                }
            return false;
        }
    });

        //support ListView or GridView
        AdapterView listView = (AdapterView) this.findViewById(R.id.messages);
        listView.setAdapter(adapter);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        // Create Inbox box URI
        Uri inboxURI = Uri.parse("content://sms/inbox");

        // List required columns
        String[] reqCols = new String[] {
                Telephony.Sms.Conversations._ID,
                Telephony.Sms.Conversations.ADDRESS,
                Telephony.Sms.Conversations.BODY,
                Telephony.Sms.Conversations.DATE
        };

        // Get Content Resolver object, which will deal with Content Provider
        //ContentResolver cr = getContentResolver();

        // Fetch Sent SMS Message from Built-in Content Provider
        //Cursor c = cr.query(inboxURI, reqCols, null, null, null);

        //use the loader solution instead since Joel said it's the better one
        CursorLoader loader = new CursorLoader(
                this.getApplicationContext(),
                inboxURI,
                reqCols,
                null,
                null,
                "_id DESC LIMIT 20");

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }

}
