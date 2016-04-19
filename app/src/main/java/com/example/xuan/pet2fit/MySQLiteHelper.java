package com.example.xuan.pet2fit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Xuan on 3/18/2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_CREATURES = "creatures";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_HEALTH = "health";
    public static final String COLUMN_ATTACK = "attack";
    public static final String COLUMN_LEVEL = "level";

    private static final String DATABASE_NAME = "creatures.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CREATURES + "("
            + COLUMN_NAME + " text primary key not null, "
            + COLUMN_HEALTH + " integer not null, "
            + COLUMN_ATTACK + " integer not null, "
            + COLUMN_LEVEL + " integer not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println(MySQLiteHelper.class.getName()
                + "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        db.execSQL("drop table if exists " + TABLE_CREATURES);
        onCreate(db);
    }

    public void dropSpecificTableAndRecreate(SQLiteDatabase db, String table_name) {
        db.execSQL("drop table if exists " + table_name);
        onCreate(db);
    }
}
