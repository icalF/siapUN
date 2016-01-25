package me.icalicul.afrizal.siapun;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ExerciseActivity extends AppCompatActivity implements OnItemSelectedListener {
  private final static long TIME_LIMIT = 7200000;
  private final static long MILLIS = 1000;
  private TaskTimer timer;
  private long remainingTime = TIME_LIMIT;

  String fileDir;
  int pkg;
  String subject;

  private List<Soal> soals;
  private Integer[] opts;
  private Spinner spinner;
  private int questionNum = 0;
  private RadioGroup.OnCheckedChangeListener checkListener;
  private Lock optionChecking = new ReentrantLock();

  public static final String SCORE = "me.icalicul.afrizal.siapun.SCORE";

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    if (position == questionNum) return;
    questionNum = position;
    updateContent(questionNum);
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {} // stub

  public void finishExercise(View view) { getScore(); }

  private void getScore() {
    int score = 0;
    for (int i = 0; i < soals.size(); ++i) {
      try {
        score += (opts[i] == soals.get(i).getAnswer() ? 100 : 0);
      } catch (NullPointerException e) {}
    }

    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
    intent.putExtra(SCORE, (double) score / soals.size());
    intent.putExtra(HighscoreMenuActivity.PAKET, pkg);
    intent.putExtra(HighscoreMenuActivity.MAPEL, subject);
    startActivity(intent);
  }

  private class TaskTimer extends CountDownTimer {
    public TextView timerText;

    TaskTimer(long millisInFuture, long countDownInterval) { super(millisInFuture, countDownInterval); }

    public void onTick(long millisUntilFinished) {
      remainingTime = millisUntilFinished;
      putText(remainingTime / 1000);
    }

    @Override
    public void onFinish() {
      ExerciseActivity activity = (ExerciseActivity) getApplicationContext();
      activity.getScore();
    }

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
    setContentView(R.layout.activity_exercise_init);
    opts = new Integer[50];

    Intent intent = getIntent();
    subject = intent.getStringExtra(HighscoreMenuActivity.MAPEL);
    pkg = intent.getIntExtra(HighscoreMenuActivity.PAKET, -1);
    fileDir = "questions/v" + pkg + "/" + subject + ".json";
  }

  @Override
  protected void onStart() {
    super.onStart();
    timer = new TaskTimer(remainingTime, MILLIS);
    soals = getContent();

    // Make listener to save checked option to shared preference
    checkListener = new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
      RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
      if (checkedRadioButton == null) return;
      while (!optionChecking.tryLock()) {}
      optionChecking.lock();
      int checkedIndex = group.indexOfChild(checkedRadioButton);
      opts[questionNum] = checkedIndex;
      optionChecking.unlock();
      }
    };
  }

  @Override
  protected void onStop() {
    super.onStop();
    timer = null;
    soals = null;
    setContentView(R.layout.activity_exercise_resume);
  }

  @Override
  public void onBackPressed() {
    new AlertDialog.Builder(this)
      .setMessage("Keluar dari latihan?")
      .setCancelable(false)
      .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          ExerciseActivity.this.finish();
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

    // Set content view
    Soal soal = soals.get(index);
    question.setText(soal.getQuestion());
    choice1.setText(soal.getChoices().get(0));
    choice2.setText(soal.getChoices().get(1));
    choice3.setText(soal.getChoices().get(2));
    choice4.setText(soal.getChoices().get(3));

    // Set checked radio
    Integer choice = opts[index];
    RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
    while (!optionChecking.tryLock()) {}
    optionChecking.lock();
    if (choice != null) {
      radioGroup.check(radioGroup.getChildAt(choice).getId());
    } else {
      radioGroup.clearCheck();
    }
    optionChecking.unlock();
  }

  public void nextQuestion(View view) {
    if(spinner.getSelectedItemPosition() < spinner.getAdapter().getCount() - 1) {
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
    setContentView(R.layout.activity_exercise_page);

    // Populate option list
    ArrayList<String> options = new ArrayList<>();
    for (int i = 0; i < soals.size(); ++i)
      options.add("Soal " + (i + 1));

    // Initialize dropdown menu
    spinner = (Spinner) findViewById(R.id.noSoal);
    spinner.setOnItemSelectedListener(this);
    ArrayAdapter<String> adapter =
      new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
    spinner.setAdapter(adapter);

    // Assign proper radiobutton listener
    RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
    radioGroup.setOnCheckedChangeListener(checkListener);

    updateContent(questionNum);
    timer.timerText = (TextView) findViewById(R.id.timerText);
    timer.start();
  }

  public List getContent() {
    AssetManager am = this.getAssets();
    String json = "";
    try {
      InputStream is = am.open(fileDir);
      InputStreamReader isr = new InputStreamReader(is, "UTF-8");
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
