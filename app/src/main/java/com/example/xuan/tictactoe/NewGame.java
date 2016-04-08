package com.example.xuan.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class NewGame extends Activity {
    GameView game_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game_view = new GameView(this);

        setContentView(R.layout.activity_new_game);
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
        Intent intent = new Intent(NewGame.this, PetInfo.class);
        startActivity(intent);
    }
}


