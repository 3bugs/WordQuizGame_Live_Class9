package com.example.wordquizgame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button playGameButton;
    private Button highScoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            int age = savedInstanceState.getInt("age");
            Toast.makeText(this, "Age = " + age, Toast.LENGTH_LONG).show();
        }

        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playGameButton = (Button) findViewById(R.id.playGameButton);

        playGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Choose difficulty level");

                dialog.setItems(
                        new String[]{"Easy", "Medium", "Hard"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("MainActivity", "You clicked " + which);

                                Intent i = new Intent(
                                        MainActivity.this,
                                        GameActivity.class
                                );
                                i.putExtra(GameActivity.EXTRA_DIFFICULTY, which);
                                startActivity(i);
                            }
                        }
                );

                dialog.show();

/*
                dialog.setPositiveButton("Easy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("MainActivity", "You clicked Easy.");
                    }
                });
                dialog.setNegativeButton("Medium", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("MainActivity", "You clicked Medium.");
                    }
                });
                dialog.setNeutralButton("Hard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("MainActivity", "You clicked Hard.");
                    }
                });
*/


            }
        });

        highScoreButton = (Button) findViewById(R.id.highScoreButton);

        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast t = Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_LONG);
                t.show();

                Toast.makeText(
                        MainActivity.this,
                        "Hello High Score",
                        Toast.LENGTH_LONG
                ).show();

                ImageView image = (ImageView) findViewById(R.id.logo_image);
                image.setImageResource(R.mipmap.ic_launcher);

                Intent intent = new Intent(MainActivity.this, HighScoreActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");

        outState.putInt("age", 40);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
