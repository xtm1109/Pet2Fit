package com.example.xuan.pet2fit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.View;

public class NewGame extends Activity {
    GameView game_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game_view = new GameView(this);

        setContentView(R.layout.activity_new_game);

        /*
         * Clear all values in SharedPreferences when user starts a new game
         */
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        game_view.resume(); // Tell the gameView resume method to execute
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        game_view.pause(); // Tell the gameView pause method to execute
    }

    public void createInfo(View view) {
        // Save the preference that there is an existing game
        // So user can continue the game
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("existed_game", true).commit();

        // Start a new game
        Intent intent = new Intent(this, PetInfo.class);
        startActivity(intent);
    }
}


