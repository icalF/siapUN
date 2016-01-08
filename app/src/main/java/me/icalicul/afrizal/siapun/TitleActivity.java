package me.icalicul.afrizal.siapun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TitleActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_title);
  }

  public void gotoExerciseMenu(View view) {
    startActivity(new Intent(getApplicationContext(), ExerciseMenuActivity.class));
  }

  public void gotoHistoryMenu(View view) {
    startActivity(new Intent(getApplicationContext(), HistoryMenuActivity.class));
  }
}
