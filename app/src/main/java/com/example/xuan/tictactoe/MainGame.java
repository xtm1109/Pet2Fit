package com.example.xuan.tictactoe;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
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

}
