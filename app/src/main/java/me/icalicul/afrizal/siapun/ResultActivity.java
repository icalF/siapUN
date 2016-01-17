package me.icalicul.afrizal.siapun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_result);

    Intent intent = getIntent();
    double score = intent.getDoubleExtra(ExerciseActivity.SCORE, 0.0);
    TextView scoreView = (TextView) findViewById(R.id.scoreView);
    scoreView.setText(String.format("%.2f", score));
  }

  @Override
  public void onBackPressed() {
    Intent intent = new Intent(getApplicationContext(), TitleActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
  }
}
