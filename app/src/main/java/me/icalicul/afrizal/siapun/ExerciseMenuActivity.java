package me.icalicul.afrizal.siapun;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class ExerciseMenuActivity extends AppCompatActivity implements OnItemSelectedListener {
  private final static long TIME_LIMIT = 7200000;
  private final static long MILLIS = 1000;
  private TaskTimer timer;
  private long remainingTime = TIME_LIMIT;
  private int questionNum = 0;
  private List<Soal> soals;
  private Spinner spinner;

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    questionNum = position;
    updateContent(questionNum);
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {} // stub

  public void finishExercise(View view) { startActivity(new Intent(getApplicationContext(), ResultActivity.class));  }

  private class TaskTimer extends CountDownTimer {
    public TextView timerText;

    TaskTimer(long millisInFuture, long countDownInterval) { super(millisInFuture, countDownInterval); }

    public void onTick(long millisUntilFinished) {
      remainingTime = millisUntilFinished;
      putText(remainingTime / 1000);
    }

    @Override
    public void onFinish() { startActivity(new Intent(getApplicationContext(), ResultActivity.class)); }

    public String putNum(Long num) { return num < 10 ? "0"+num : num.toString(); }

    public void putText(long remainingTime) {
      long remainingHours = remainingTime / 3600;
      long remainingMinutes = (remainingTime -= (remainingHours * 3600)) / 60;
      long remainingSeconds = remainingTime - remainingMinutes * 60;
      String timeView = putNum(remainingHours) + " : " + putNum(remainingMinutes) + " : " + putNum(remainingSeconds);
      timerText.setText(timeView);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exercise_menu_before);
  }

  @Override
  protected void onStart() {
    super.onStart();
    timer = new TaskTimer(remainingTime, MILLIS);
    soals = getContent();
  }

  @Override
  protected void onStop() {
    super.onStop();
    timer = null;
    soals = null;
    setContentView(R.layout.activity_exercise_menu_stopped);
  }

  @Override
  public void onBackPressed() {
    new AlertDialog.Builder(this)
      .setMessage("Keluar dari latihan?")
      .setCancelable(false)
      .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          ExerciseMenuActivity.this.finish();
        }
      })
      .setNegativeButton("Tidak", null)
      .show();
  }

  private void updateContent(int index) {
    TextView question = (TextView) findViewById(R.id.question);
    RadioButton choice1 = (RadioButton) findViewById(R.id.choice1);
    RadioButton choice2 = (RadioButton) findViewById(R.id.choice2);
    RadioButton choice3 = (RadioButton) findViewById(R.id.choice3);
    RadioButton choice4 = (RadioButton) findViewById(R.id.choice4);
    RadioButton choice5 = (RadioButton) findViewById(R.id.choice5);

    Soal soal = soals.get(index);

    question.setText(soal.getQuestion());
    choice1.setText(soal.getChoices().get(0));
    choice2.setText(soal.getChoices().get(1));
    choice3.setText(soal.getChoices().get(2));
    choice4.setText(soal.getChoices().get(3));
    choice5.setText(soal.getChoices().get(4));
  }

  public void nextQuestion(View view) {
    if(spinner.getSelectedItemPosition() < spinner.getAdapter().getCount()) {
      spinner.setSelection(spinner.getSelectedItemPosition() + 1);
      updateContent(++questionNum);
    }
  }
  public void prevQuestion(View view) {
    if(spinner.getSelectedItemPosition() > 0) {
      spinner.setSelection(spinner.getSelectedItemPosition() - 1);
      updateContent(--questionNum);
    }
  }

  public void hasReady(View view) {
    setContentView(R.layout.activity_exercise_menu);

    // Initialize dropdown menu
    spinner = (Spinner) findViewById(R.id.noSoal);
    spinner.setOnItemSelectedListener(this);
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
      R.array.no_soal, android.R.layout.simple_spinner_item);
    spinner.setAdapter(adapter);

    updateContent(questionNum);
    timer.timerText = (TextView) findViewById(R.id.timerText);
    timer.start();
  }

  public List getContent() {
    AssetManager am = this.getAssets();
    String json = "";
    try {
      InputStream is = am.open("loli.json");
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);

      for (String s = br.readLine(); s != null; s = br.readLine()) {
        json += s;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    List<Soal> content = new LinkedList<>();
    try {
      JSONArray soals = new JSONArray(json);
      for (int i = 0; i < soals.length(); ++i) {
        content.add(new Soal(soals.getJSONObject(i)));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return content;
  }
}
