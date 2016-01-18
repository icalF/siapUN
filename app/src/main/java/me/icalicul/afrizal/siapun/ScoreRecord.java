package me.icalicul.afrizal.siapun;

/**
 * Created by Afrizal on 1/18/2016.
 */
public class ScoreRecord {
  private String subject;
  private double score;

  public ScoreRecord(String subject, double score) {
    this.subject = subject;
    this.score = score;
  }

  public String getSubject() {
    return subject;
  }

  @Override
  public String toString() {
    return "ScoreRecord{" +
      "subject='" + subject + '\'' +
      ", score=" + score +
      '}';
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public double getScore() {
    return score;
  }

  public void setScore(double score) {
    this.score = score;
  }
}
