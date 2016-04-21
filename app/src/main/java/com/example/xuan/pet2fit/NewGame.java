package com.example.xuan.pet2fit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.View;

public class NewGame extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_game);
    }

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

    }

    public void createInfo(View view) {
        // Save the preference that there is an existing game
        // So user can continue the game
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("existed_game", true).commit();

        // Start a new game
        Intent intent = new Intent(this, PetInfo.class);
        PetOptionsFragment fr = (PetOptionsFragment) getFragmentManager().findFragmentById(R.id.pet_options_frag);
        intent.putExtra("pet_option", fr.current_choice);
        startActivity(intent);
    }
}


