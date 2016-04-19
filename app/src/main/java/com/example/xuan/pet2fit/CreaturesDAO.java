package com.example.xuan.pet2fit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Xuan on 3/18/2016.
 */
public class CreaturesDAO {
    private SQLiteDatabase database;
    private MySQLiteHelper db_helper;
    private String[] all_columns = {MySQLiteHelper.COLUMN_NAME,
            MySQLiteHelper.COLUMN_HEALTH,
            MySQLiteHelper.COLUMN_ATTACK,
            MySQLiteHelper.COLUMN_LEVEL};

    public CreaturesDAO(Context context) {
        db_helper = new MySQLiteHelper(context);
    }

    public void open() {
        database = db_helper.getWritableDatabase();
    }

    public void close() {
        db_helper.close();
    }

    public boolean createCreature(String n, int h, int a, int lv) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, n);
        values.put(MySQLiteHelper.COLUMN_HEALTH, h);
        values.put(MySQLiteHelper.COLUMN_ATTACK, a);
        values.put(MySQLiteHelper.COLUMN_LEVEL, lv);

        if (database.insert(MySQLiteHelper.TABLE_CREATURES, null, values) != -1) {
            return true;
        }
        else {
            return false;
        }
    }

    public void deleteCreature(AICreature c) {
        String name = c.getName();
        System.out.println("Creature \"" + name + "\" is deleted");
        database.delete(MySQLiteHelper.TABLE_CREATURES,
                MySQLiteHelper.COLUMN_NAME + " = " + name, null);
    }

    public AICreature getCreatureWithSpecificLevel(int lv) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CREATURES, all_columns,
                MySQLiteHelper.COLUMN_LEVEL + " = " + lv,
                null, null, null, null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            AICreature creature = new AICreature();

            creature.setName(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_NAME)));
            creature.setHealth(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_HEALTH)));
            creature.setAttack(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_ATTACK)));
            creature.setLevel(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_LEVEL)));

            return creature;
        }

        return null;
    }

    public void dropTableAndRecreate() {
        db_helper.dropSpecificTableAndRecreate(database, MySQLiteHelper.TABLE_CREATURES);
    }

}
