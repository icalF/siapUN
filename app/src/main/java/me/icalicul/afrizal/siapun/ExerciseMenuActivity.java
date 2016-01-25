package me.icalicul.afrizal.siapun;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class ExerciseMenuActivity extends AppCompatActivity {
  private List<Button> selector;
  private View.OnClickListener selectorHandler;
  private String mapel;
  private int paket;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exercise_menu);

    selectorHandler = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int id = Integer.parseInt(
          v.getResources().getResourceName(v.getId()).substring(35, 36)
        );
        paket = id;

        // Set new text
        String[] mapels = HighscoreMenuActivity.mapels;
        for (int i = 0; i < selector.size(); ++i)
          selector.get(i).setText(mapels[i]);

        // Replace itself
        selectorHandler = new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            int id = Integer.parseInt(
              v.getResources().getResourceName(v.getId()).substring(35, 36)
            );
            mapel = HighscoreMenuActivity.mapelCodes[id - 1];

            // Call callback function
            Intent intent = new Intent(getApplicationContext(), ExerciseActivity.class);
            intent.putExtra(HighscoreMenuActivity.PAKET, paket);
            intent.putExtra(HighscoreMenuActivity.MAPEL, mapel);
            startActivity(intent);
          }
        };
        for (int i = 0; i < selector.size(); ++i)
          selector.get(i).setOnClickListener(selectorHandler);
      }
    };

    selector = new ArrayList<>(4);
    selector.add((Button) findViewById(R.id.paket1e));
    selector.add((Button) findViewById(R.id.paket2e));
    selector.add((Button) findViewById(R.id.paket3e));
    selector.add((Button) findViewById(R.id.paket4e));
    for (int i = 0; i < selector.size(); ++i)
      selector.get(i).setOnClickListener(selectorHandler);
  }
}
