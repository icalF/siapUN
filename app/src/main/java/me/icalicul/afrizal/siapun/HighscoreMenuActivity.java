package me.icalicul.afrizal.siapun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class HighscoreMenuActivity extends AppCompatActivity {
  private List<ScoreRecord> scoreRecordList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_highscore_menu);

    scoreRecordList = new StatisticsDbHelper(getApplicationContext()).getScores();

  }
}
