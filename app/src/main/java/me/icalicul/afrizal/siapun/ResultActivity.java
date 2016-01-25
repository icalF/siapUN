package me.icalicul.afrizal.siapun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Random;

public class ResultActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_result);

    Intent intent = getIntent();
    final double score = intent.getDoubleExtra(ExerciseActivity.SCORE, 0.0);
    final String subject = intent.getStringExtra(HighscoreMenuActivity.MAPEL);
    final int pkg = intent.getIntExtra(HighscoreMenuActivity.PAKET, -1);

    new Thread() {
      @Override
      public void run() {
        // Save score to database
        new StatisticsDbHelper(getApplicationContext())
          .insertScore(subject, pkg, score);
      }
    }.start();

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
  }

  @Override
  // Clear activity stacks back to the Title Page
  public void onBackPressed() {
    Intent intent = new Intent(getApplicationContext(), TitleActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    startActivity(intent);
  }
}
