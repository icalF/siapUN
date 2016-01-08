package me.icalicul.afrizal.siapun;

import java.util.List;

/**
 * Created by Afrizal on 1/8/2016.
 */
public class Soal {
  private String question;
  private List<String> choices;
  private int answer;

  public Soal(String question, List<String> choices, int answer) {
    this.answer = answer;
    this.question = question;
    this.choices = choices;
  }

  public String getQuestion() {
    return question;
  }

  public List<String> getChoices() {
    return choices;
  }

  public int getAnswer() {
    return answer;
  }
}
