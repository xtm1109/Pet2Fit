package com.example.xuan.tictactoe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;

public class MainGame extends Activity {
    MainGameView main_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        main_view = new MainGameView(this);
        main_view.setBackgroundColor(Color.WHITE);

        setContentView(main_view);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Timestamp when the app is closed
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putLong("last_used",System.currentTimeMillis()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Timestamp when the app is re-opened
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        long currentTime = System.currentTimeMillis();
        long lastTime = prefs.getLong("last_used", currentTime);

        // Calculate new pet's health
        long been_away = ((currentTime - lastTime)/1000); // to seconds
        ThePet.setCurrentHealth(ThePet.getCurrentHealth() - (int) been_away);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(MainGame.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
