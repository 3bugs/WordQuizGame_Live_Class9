package com.example.wordquizgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";
    public static final String EXTRA_DIFFICULTY = "diff";

    private int mDifficulty;

    private ArrayList<String> mFileNameList = new ArrayList<>();
    private ArrayList<String> mQuizWordList = new ArrayList<>();
    private ArrayList<String> mChoiceWordList = new ArrayList<>();
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent i = getIntent();
        mDifficulty = i.getIntExtra(EXTRA_DIFFICULTY, 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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
