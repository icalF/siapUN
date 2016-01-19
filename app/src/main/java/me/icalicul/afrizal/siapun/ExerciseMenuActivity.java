package me.icalicul.afrizal.siapun;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ExerciseMenuActivity extends AppCompatActivity {
  private Spinner spinnerPaket;
  private Spinner spinnerMapel;

  public static final String FILEDIR = "me.icalicul.afrizal.siapun.FILEDIR";
  public static final String SUBJECT = "me.icalicul.afrizal.siapun.SUBJECT";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exercise_menu);

    // Fill spinners contents
    spinnerPaket = (Spinner) findViewById(R.id.spinnerPaket);
    ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
      this, R.array.spinner_paket, android.R.layout.simple_spinner_item);
    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerPaket.setAdapter(adapter1);

    spinnerMapel = (Spinner) findViewById(R.id.spinnerMapel);
    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
      this, R.array.spinner_mapel, android.R.layout.simple_spinner_item);
    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerMapel.setAdapter(adapter2);
  }

  public void beginExercise(View view) {
    int paket = spinnerPaket.getSelectedItemPosition();
    int mapel = spinnerMapel.getSelectedItemPosition();

    if (paket == 0 || mapel == 0) {
      final AlertDialog alert = new AlertDialog.Builder(this)
        .setMessage("Pilih paket dan mata pelajaran terlebih dahulu")
        .setCancelable(true)
        .create();

      new Handler().postDelayed(new Runnable() {
        public void run() {
          alert.dismiss();
        }
      }, 800);    // set timeout 0.8 s
    } else {
      String[] mapelList = {"ind", "eng", "mat", "fis", "kim", "bio"};
      String fileDir = "questions/v";
      String subject = mapelList[mapel - 1];
      fileDir += paket;
      fileDir += "/" + subject + ".json";

      Intent intent = new Intent(getApplicationContext(), ExerciseActivity.class);
      intent.putExtra(FILEDIR, fileDir);
      intent.putExtra(SUBJECT, subject);
      startActivity(intent);
    }
  }
}
