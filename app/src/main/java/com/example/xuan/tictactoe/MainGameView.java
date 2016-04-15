package com.example.xuan.tictactoe;

import android.content.Context;
import android.graphics.*;
import android.view.View;

/**
 * Created by Xuan on 4/11/2016.
 */
public class MainGameView extends View {
    Paint paint = new Paint();

    int bar_x = 50;
    int bar_width = 500;
    int bar_height = 60;

    RectF h_bar = new RectF(bar_x, bar_x, bar_x+bar_width, bar_x+bar_height); // First bar: x = y
    RectF s_bar = new RectF(bar_x, h_bar.bottom+10, bar_x+bar_width, h_bar.bottom+10+bar_height);
    RectF xp_bar = new RectF(bar_x, s_bar.bottom+10, bar_x+bar_width, s_bar.bottom+10+bar_height);

    public MainGameView(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawHealthBar(canvas);
        drawStaminaBar(canvas);
        drawXPBar(canvas);
    }

    private void drawHealthBar(Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        canvas.drawRect(h_bar, paint);
        paint.setStrokeWidth(0);
        paint.setColor(Color.CYAN);
        canvas.drawRect(h_bar, paint);
        paint.setColor(Color.YELLOW);
        canvas.drawRect(h_bar, paint);
    }

    private void drawStaminaBar(Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        canvas.drawRect(s_bar, paint);
        paint.setStrokeWidth(0);
        paint.setColor(Color.CYAN);
        canvas.drawRect(s_bar, paint);
        paint.setColor(Color.YELLOW);
        canvas.drawRect(s_bar, paint );
    }

    private void drawXPBar(Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        canvas.drawRect(xp_bar, paint);
        paint.setStrokeWidth(0);
        paint.setColor(Color.CYAN);
        canvas.drawRect(xp_bar, paint);
        paint.setColor(Color.YELLOW);
        canvas.drawRect(xp_bar, paint );
    }
}
