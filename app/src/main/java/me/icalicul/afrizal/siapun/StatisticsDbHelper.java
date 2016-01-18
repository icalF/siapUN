package me.icalicul.afrizal.siapun;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Afrizal on 1/18/2016.
 */
public class StatisticsDbHelper extends SQLiteOpenHelper {

  private static final String TEXT_TYPE = " TEXT";
  private static final String REAL_TYPE = " REAL";
  private static final String COMMA_SEP = ",";
  private static final String SQL_CREATE_ENTRIES =
    "CREATE TABLE " + StatisticsSchema.StatisticEntry.TABLE_NAME + " (" +
      StatisticsSchema.StatisticEntry._ID + " INTEGER PRIMARY KEY," +
      StatisticsSchema.StatisticEntry.COLUMN_NAME_SUBJECT + TEXT_TYPE + COMMA_SEP +
      StatisticsSchema.StatisticEntry.COLUMN_NAME_SCORE + REAL_TYPE +
      " )";
  private static final String SQL_DELETE_ENTRIES =
    "DROP TABLE IF EXISTS " + StatisticsSchema.StatisticEntry.TABLE_NAME;

  // If you change the database schema, you must increment the database version.
  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "Statistics.db";

  public StatisticsDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public void onCreate(SQLiteDatabase db) {
    db.execSQL(SQL_CREATE_ENTRIES);
  }

  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // This database is only a cache for online data, so its upgrade policy is
    // to simply to discard the data and start over
    db.execSQL(SQL_DELETE_ENTRIES);
    onCreate(db);
  }

  public void insertScore(String subject, double score) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(StatisticsSchema.StatisticEntry.COLUMN_NAME_SUBJECT, subject);
    contentValues.put(StatisticsSchema.StatisticEntry.COLUMN_NAME_SCORE, score);
    long l = db.insert(StatisticsSchema.StatisticEntry.TABLE_NAME, null, contentValues);
  }

  public List<ScoreRecord> getScores() {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor res =  db.rawQuery( "SELECT * FROM " + StatisticsSchema.StatisticEntry.TABLE_NAME, null );
    res.moveToFirst();

    List<ScoreRecord> list = new ArrayList<>();
    while(!res.isAfterLast()){
      list.add( new ScoreRecord(
        res.getString(res.getColumnIndex(StatisticsSchema.StatisticEntry.COLUMN_NAME_SUBJECT)),
        res.getDouble(res.getColumnIndex(StatisticsSchema.StatisticEntry.COLUMN_NAME_SCORE))
      ));
      res.moveToNext();
    }

    return list;
  }
}
