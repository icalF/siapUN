package me.icalicul.afrizal.siapun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class HighscoreMenuActivity extends AppCompatActivity {
  private List<Button> selector;
  private View.OnClickListener selectorHandler;
  private String mapel;
  private int paket;

  public final static String MAPEL = "me.icalicul.afrizal.siapun.MAPEL";
  public final static String PAKET = "me.icalicul.afrizal.siapun.PAKET";
  public final static String[] mapelCodes = {"ind", "eng", "mat", "ipa"};
  public final static String[] mapels = {
    "Bahasa Indonesia",
    "Bahasa Inggris",
    "Matematika",
    "IPA"
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_highscore_menu);

    selectorHandler = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int id = Integer.parseInt(
          v.getResources().getResourceName(v.getId()).substring(35, 36)
        );
        paket = id;

        // Set new text
        for (int i = 0; i < selector.size(); ++i)
          selector.get(i).setText(mapels[i]);

        // Replace itself
        selectorHandler = new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            int id = Integer.parseInt(
              v.getResources().getResourceName(v.getId()).substring(35, 36)
            );
            mapel = mapelCodes[id - 1];

            // Call callback function
            Intent intent = new Intent(getApplicationContext(), HighscoreActivity.class);
            intent.putExtra(PAKET, paket);
            intent.putExtra(MAPEL, mapel);
            startActivity(intent);
          }
        };
        for (int i = 0; i < selector.size(); ++i)
          selector.get(i).setOnClickListener(selectorHandler);
      }
    };

    selector = new ArrayList<>(4);
    selector.add((Button) findViewById(R.id.paket1h));
    selector.add((Button) findViewById(R.id.paket2h));
    selector.add((Button) findViewById(R.id.paket3h));
    selector.add((Button) findViewById(R.id.paket4h));
    for (int i = 0; i < selector.size(); ++i)
      selector.get(i).setOnClickListener(selectorHandler);
  }

}
