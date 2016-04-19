package me.icalicul.afrizal.siapun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class ResultActivity extends AppCompatActivity {
  TextView scoreView;
  long millisStart = System.currentTimeMillis();
  long millisEnd;

  double score;
  String subject;
  int pkg;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_result);
  }

  @Override
  protected void onStart() {
    super.onStart();

    Intent intent = getIntent();
    score = intent.getDoubleExtra(ExerciseActivity.SCORE, 0.0);
    subject = intent.getStringExtra(HighscoreMenuActivity.MAPEL);
    pkg = intent.getIntExtra(HighscoreMenuActivity.PAKET, -1);

    scoreView = (TextView) findViewById(R.id.scoreView);
    new Thread() {
      @Override
      public void run() {
        // Save score to database
        new StatisticsDbHelper(getApplicationContext())
                .insertScore(subject, pkg, score);
      }
    }.start();
  }

  @Override
  protected void onResume() {
    super.onResume();

    // Make random effect
    Thread t = new Thread() {
      @Override
      public void run() {
        try {
          while (millisEnd - millisStart < 5000) {
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                Random rn = new Random();
                int n = 100;
                double i = rn.nextDouble() * n;
                scoreView.setText(String.format("%.2f", i));
                millisEnd = System.currentTimeMillis();
              }
            });
            Thread.sleep(100);
          }
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              scoreView.setText(String.format("%.2f", score));
            }
          });
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
    t.start();
  }

  public void gotoHistory(View view) {
    Intent intent = new Intent(getApplicationContext(), HighscoreActivity.class);
    intent.putExtra(HighscoreMenuActivity.MAPEL, subject);
    intent.putExtra(HighscoreMenuActivity.PAKET, pkg);
    startActivity(intent);
  }

  private void back() {
    Intent intent = new Intent(getApplicationContext(), TitleActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    startActivity(intent);
  }

  @Override
  // Clear activity stacks back to the Title Page
  public void onBackPressed() {
    back();
  }

  public void back(View view) {
    back();
  }
}
