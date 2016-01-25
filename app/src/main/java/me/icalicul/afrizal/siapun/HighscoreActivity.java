package me.icalicul.afrizal.siapun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HighscoreActivity extends AppCompatActivity {
  private ArrayList<String> scoreRecordList;
  private String mapel;
  private int paket;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_highscore);

    Intent intent = getIntent();
    mapel = intent.getStringExtra(HighscoreMenuActivity.MAPEL);
    paket = intent.getIntExtra(HighscoreMenuActivity.PAKET, -1);

    scoreRecordList = new StatisticsDbHelper(getApplicationContext()).getScores(paket, mapel);

    ArrayAdapter<String> adapter =
      new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scoreRecordList);
    ListView list = (ListView) findViewById(R.id.listView);
    list.setAdapter(adapter);
  }
}
