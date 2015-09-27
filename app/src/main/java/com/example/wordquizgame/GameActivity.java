package com.example.wordquizgame;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.wordquizgame.db.DatabaseHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";
    public static final String EXTRA_DIFFICULTY = "diff";

    private int mDifficulty;

    private ArrayList<String> mFileNameList = new ArrayList<>();
    private ArrayList<String> mQuizWordList = new ArrayList<>();
    private ArrayList<String> mChoiceWordList = new ArrayList<>();

    private TextView mQuestionNumberTextView;
    private ImageView mQuestionImageView;
    private TableLayout mButtonTableLayout;
    private TextView mAnswerTextView;

    private Random mRandom = new Random();
    private Handler mHandler = new Handler();

    private String mAnswerFileName;
    private int mTotalGuesses;
    private int mScore;
    private int mNumChoices;

    private DatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mHelper = new DatabaseHelper(this);
        mDatabase = mHelper.getWritableDatabase();

        Intent i = getIntent();
        mDifficulty = i.getIntExtra(EXTRA_DIFFICULTY, 0);

        switch (mDifficulty) {
            case 0:
                mNumChoices = 2;
                break;
            case 1:
                mNumChoices = 4;
                break;
            case 2:
                mNumChoices = 6;
                break;
        }

        mQuestionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        mQuestionImageView = (ImageView) findViewById(R.id.questionImageView);
        mButtonTableLayout = (TableLayout) findViewById(R.id.buttonTableLayout);
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);

        getImageFileNames();
    }

    private void getImageFileNames() {
        String[] categories = {"animals", "body", "colors", "numbers", "objects"};

        AssetManager assets = getAssets();

        for (String category : categories) {
            try {
                String[] fileNames = assets.list(category);

                for (String f : fileNames) {
                    mFileNameList.add(f.replace(".png", ""));
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error listing filename in " + category);
            }
        }

        // log ชื่อไฟล์รูปภาพทั้ง 50 ไฟล์ออกมาดู
        Log.i(TAG, "*******************************");
        Log.i(TAG, "รายชื่อไฟล์ทั้งหมด");
        for (String f : mFileNameList) {
            Log.i(TAG, f);
        }
        Log.i(TAG, "*******************************");

        startQuiz();
    }

    private void startQuiz() {
        mTotalGuesses = 0;
        mScore = 0;
        mQuizWordList.clear();

        while (mQuizWordList.size() < 3) {
            int randomIndex = mRandom.nextInt(mFileNameList.size());
            String fileName = mFileNameList.get(randomIndex);

            if (mQuizWordList.contains(fileName) == false) {
                mQuizWordList.add(fileName);
            }
        }

        // log ชื่อไฟล์รูปภาพคำถาม
        Log.i(TAG, "*******************************");
        Log.i(TAG, "รายชื่อไฟล์คำถาม");
        for (String f : mQuizWordList) {
            Log.i(TAG, f);
        }
        Log.i(TAG, "*******************************");

        loadNextQuestion();
    }

    private void loadNextQuestion() {
        mAnswerTextView.setText(null);
        mAnswerFileName = mQuizWordList.remove(0);

        String msg = String.format("คำถาม %d จาก 3", mScore + 1);
        mQuestionNumberTextView.setText(msg);

        Log.i(TAG, "ไฟล์รูปภาพคำถามคือ " + mAnswerFileName);

        loadQuestionImage();
        prepareChoiceWords();
    }

    private void loadQuestionImage() {
        String category = mAnswerFileName.substring(
                0,
                mAnswerFileName.indexOf('-')
        );
        String filePath = category + "/" + mAnswerFileName + ".png";

        AssetManager assets = getAssets();

        InputStream stream;
        try {
            stream = assets.open(filePath);
            Drawable drawable = Drawable.createFromStream(stream, filePath);
            mQuestionImageView.setImageDrawable(drawable);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error loading image file: " + filePath);
        }
    }

    private void prepareChoiceWords() {
        mChoiceWordList.clear();

        while (mChoiceWordList.size() < mNumChoices) {
            int randomIndex = mRandom.nextInt(mFileNameList.size());
            String randomWord = getWord(mFileNameList.get(randomIndex));

            if (mChoiceWordList.contains(randomWord) == false &&
                    randomWord.equals(getWord(mAnswerFileName)) == false) {
                mChoiceWordList.add(randomWord);
            }
        }

        int randomIndex = mRandom.nextInt(mNumChoices);
        mChoiceWordList.set(randomIndex, getWord(mAnswerFileName));

        // log คำศัพท์ที่เป็น choice
        Log.i(TAG, "*******************************");
        Log.i(TAG, "คำศัพท์ตัวเลือกที่สุ่มได้");
        for (String w : mChoiceWordList) {
            Log.i(TAG, w);
        }
        Log.i(TAG, "*******************************");

        createChoiceButtons();
    }

    private void createChoiceButtons() {
        for (int row = 0; row < mButtonTableLayout.getChildCount(); row++) {
            TableRow tr = (TableRow) mButtonTableLayout.getChildAt(row);
            tr.removeAllViews();
        }

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int row = 0; row < mNumChoices / 2; row++) {
            TableRow tr = (TableRow) mButtonTableLayout.getChildAt(row);

            for (int column = 0; column < 2; column++) {
                Button guessButton = (Button) inflater.inflate(R.layout.guess_button, tr, false);
                guessButton.setText(mChoiceWordList.remove(0));
                guessButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitGuess((Button) v);
                    }
                });
                tr.addView(guessButton);
            }
        }

    }

    private void submitGuess(Button guessButton) {
        Log.i(TAG, "You selected " + guessButton.getText().toString());

        mTotalGuesses++;

        String guessWord = guessButton.getText().toString();
        String answerWord = getWord(mAnswerFileName);

        // ตอบถูก
        if (guessWord.equals(answerWord)) {
            mScore++;

            MediaPlayer mp = MediaPlayer.create(this, R.raw.applause);
            mp.start();

            String msg = guessWord + " ถูกต้องนะคร้าบบ";
            mAnswerTextView.setText(msg);
            mAnswerTextView.setTextColor(
                    getResources().getColor(android.R.color.holo_green_dark));

            // ตอบถูก และเล่นครบทุกข้อแล้ว (จบเกม)
            if (mScore == 3) {

                saveScore();

                String msgResult = String.format(
                        "จำนวนครั้งที่ทาย: %d\nเปอร์เซ็นต์ความถูกต้อง: %.1f",
                        mTotalGuesses,
                        100 * 3 / (double) mTotalGuesses
                );

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("สรุปผล");
                dialog.setMessage(msgResult);
                dialog.setCancelable(false);
                dialog.setPositiveButton("เริ่มเกมใหม่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startQuiz();
                    }
                });
                dialog.setNegativeButton("กลับหน้าหลัก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dialog.show();
            }
            // ตอบถูก แต่ยังไม่ครบทุกข้อ (ยังไม่จบเกม)
            else {
                mHandler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                loadNextQuestion();
                            }
                        }
                        , 2000
                );
            }
        }
        // ตอบผิด
        else {
            guessButton.setEnabled(false);

            MediaPlayer mp = MediaPlayer.create(this, R.raw.fail3);
            mp.start();

            String msg = "ผิดครับ ลองใหม่นะครับ";
            mAnswerTextView.setText(msg);
            mAnswerTextView.setTextColor(
                    getResources().getColor(android.R.color.holo_red_dark));
        }

    }

    private void saveScore() {

        ContentValues cv = new ContentValues();
        double percent = 100 * 3 / (double) mTotalGuesses;

        cv.put(DatabaseHelper.COL_SCORE, percent);
        cv.put(DatabaseHelper.COL_DIFFICULTY, mDifficulty);

        mDatabase.insert(DatabaseHelper.TABLE_NAME, null, cv);

    }

    private String getWord(String fileName) {
        String word = fileName.substring(
                fileName.indexOf('-') + 1
        );

        return word;
    }


/*
    private OkHttpClient client = new OkHttpClient();

    private void connectInternet(String url) {
        final Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {


                String result = response.body().string();
                Log.i(TAG, result);
            }
        });
    }
*/
}
