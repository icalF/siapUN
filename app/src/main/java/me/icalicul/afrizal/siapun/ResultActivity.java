package me.icalicul.afrizal.siapun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class ResultActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_result);

    Intent intent = getIntent();
    final double score = intent.getDoubleExtra(ExerciseActivity.SCORE, 0.0);
    final String subject = intent.getStringExtra(ExerciseMenuActivity.SUBJECT);

    // Make random effect
    final TextView scoreView = (TextView) findViewById(R.id.scoreView);
    long millisStart = System.currentTimeMillis();
    long millisEnd;
    do {
      Random rn = new Random();
      int n = 10001;
      int i = rn.nextInt() % n;
      scoreView.setText(String.format("%.2f", (double)i/100));
      millisEnd = System.currentTimeMillis();
    } while (millisEnd - millisStart < 2000);
    scoreView.setText(String.format("%.2f", score));

    new Thread() {
      public void run() {
        // Save score to database
        StatisticsDbHelper dbHelper = new StatisticsDbHelper(getApplicationContext());
        dbHelper.insertScore(subject, score);
      }
    }.start();

    // Debug
    StatisticsDbHelper dbHelper = new StatisticsDbHelper(getApplicationContext());
    List<ScoreRecord> l = dbHelper.getScores();
    for (ScoreRecord s : l)
      Log.d(ExerciseMenuActivity.DEBUG, String.valueOf(s));
  }

  @Override
  public void onBackPressed() {
    Intent intent = new Intent(getApplicationContext(), TitleActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    startActivity(intent);
  }
}
