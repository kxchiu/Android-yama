package edu.uw.cwc8.yama;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

/*
* Allow the user to see a list of messages received.
* Display the author, body, and sent date of the message.
* Resource used: http://pulse7.net/android/read-sms-message-inbox-sent-draft-android/
 */

public class ReadingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "ReadingActivity";
    //private ArrayAdapter<String> adapter;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        ArrayList<String> list = new ArrayList<String>();

        //controller
        String[] cols = new String[]{
                Telephony.TextBasedSmsColumns.BODY,
                Telephony.TextBasedSmsColumns.CREATOR};
        int[] ids = new int[]{R.id.txtItem};

        adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item,
                null,
                cols, ids,
                0 //no flag
        );

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
        String[] reqCols = new String[] { "_id", "person", "address", "date", "body"};

        // Get Content Resolver object, which will deal with Content Provider
        //ContentResolver cr = getContentResolver();

        // Fetch Sent SMS Message from Built-in Content Provider
        //Cursor c = cr.query(inboxURI, reqCols, null, null, null);

        //use the loader solution instead since Joel said it's the better one
        CursorLoader loader = new CursorLoader(
                this.getApplicationContext(),
                inboxURI,
                reqCols,
                null, null,
                "_id ASC LIMIT 20");

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        Cursor cr = adapter.getCursor();
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }

}
