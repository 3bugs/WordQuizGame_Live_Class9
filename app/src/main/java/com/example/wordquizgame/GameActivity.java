package com.example.wordquizgame;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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
                    randomWord.equals(getWord(mAnswerFileName)) == false ) {
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
