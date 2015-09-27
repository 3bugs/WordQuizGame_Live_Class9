package com.example.wordquizgame;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.wordquizgame.db.DatabaseHelper;

public class HighScoreActivity extends AppCompatActivity {

    private DatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        mHelper = new DatabaseHelper(this);
        mDatabase = mHelper.getWritableDatabase();

        Cursor cursor = mDatabase.rawQuery("SELECT * FROM scores WHERE score > 70", null);

        while (cursor.moveToNext()) {
            Double score = cursor.getDouble(cursor.getColumnIndex("score"));

        }

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[] { "score", "difficulty" },
                new int[] { android.R.id.text1, android.R.id.text2 },
                0
        );

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_high_score, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
