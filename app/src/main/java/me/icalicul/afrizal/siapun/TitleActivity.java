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
    findViewById(R.id.latihanMainMenu).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(TitleActivity.this, ExerciseMenuActivity.class));
      }
    });
    findViewById(R.id.riwayatMainMenu).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(TitleActivity.this, HistoryMenuActivity.class));
      }
    });
  }

  public void gotoExerciseMenu(View view) {
    startActivity(new Intent(getApplicationContext(), ExerciseMenuActivity.class));
  }

  public void gotoHistoryMenu(View view) {
    startActivity(new Intent(getApplicationContext(), HistoryMenuActivity.class));
  }
}
