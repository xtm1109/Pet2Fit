package com.example.xuan.tictactoe;

import android.content.Context;
import android.graphics.*;
import android.view.View;

/**
 * Created by Xuan on 4/11/2016.
 */
public class MainGameView extends View {
    Paint paint = new Paint();

    int margin_x;
    int margin_y;

    int bar_width;
    int bar_height;

    float text_size;

    public MainGameView(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        margin_x = getWidth()/20;
        margin_y = getHeight()/60;

        bar_width = (getWidth()-(margin_x*2))/3;
        bar_height = (getHeight()-(margin_y*2))/27;

        // Set text size depends on the screen resolution
        text_size = 13*getResources().getDisplayMetrics().density;

        drawHealthBar(canvas);
        drawStaminaBar(canvas);
//        drawXPBar(canvas);
    }

    private void drawHealthBar(Canvas canvas) {
        // A RectF for text
        RectF text_box = new RectF(margin_x, margin_y,
                margin_x+bar_width, margin_y+bar_height);
        // A RectF for bar
        RectF h_bar = new RectF(text_box.left, text_box.bottom,
                text_box.left+bar_width, text_box.bottom+bar_height);
        // A Rect that bounds around the text
        Rect text_bound = new Rect();
        String text = "Health: "
                + (100.0*ThePet.getCurrentHealth()/ThePet.getLevelHealth())
                + "%"; // The text

        /* ~~~ Draw border for bar ~~~ */
        paint.setStrokeWidth(7);
        paint.setStyle(Paint.Style.STROKE); // Stroke = no fill
        paint.setColor(Color.BLACK);
        // drawRect draws at left, top, right, bottom
        canvas.drawRect(h_bar, paint);
        /* ~~~ DONE ~~~ */

        /* ~~~ Draw a bar that presents health ~~~ */
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.CYAN);
        // drawRect draws at left, top, right, bottom
        canvas.drawRect(h_bar, paint);
        /* ~~~ DONE ~~~ */

        /* ~~~ Draw current health of the pet ~~~ */
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.MAGENTA);
        // drawRect draws at left, top, right, bottom
        // Fill in the health bar depends of the current health of the pet
        canvas.drawRect(h_bar.left, h_bar.top,
                h_bar.left + ((ThePet.getCurrentHealth() * bar_width) / ThePet.getLevelHealth()),
                h_bar.bottom, paint);
        /* ~~~ DONE ~~~ */

        /* ~~~ Draw text ~~~ */
        paint.setColor(Color.BLACK);
        paint.setTextSize(text_size);
        paint.getTextBounds(text, 0, text.length(), text_bound);
        // drawText draws at left, bottom
        // That is, it starts drawing at the bottom left corner of a rect
        canvas.drawText(text, text_box.left + (text_box.width()-text_bound.width())/2,
                text_box.bottom - (text_box.height()-text_bound.height())/2, paint);
        /* ~~~ DONE ~~~ */
    }

    private void drawStaminaBar(Canvas canvas) {
        // A RectF for text
        RectF text_box = new RectF(margin_x+bar_width*2, margin_y,
                getWidth()-margin_x, margin_y+bar_height);
        // A RectF for bar
        RectF s_bar = new RectF(text_box.left, text_box.bottom,
                text_box.left+bar_width, text_box.bottom+bar_height);
        // A Rect that bounds around the text
        Rect text_bound = new Rect();
        String text = "Stamina: "
                + (100.0*ThePet.getCurrentStamina()/ThePet.getLevelStamina())
                + "%"; // The text

        /* ~~~ Draw border for bar ~~~ */
        paint.setStrokeWidth(7);
        paint.setStyle(Paint.Style.STROKE); // Stroke = no fill
        paint.setColor(Color.BLACK);
        // drawRect draws at left, top, right, bottom
        canvas.drawRect(s_bar, paint);
        /* ~~~ DONE ~~~ */

        /* ~~~ Draw a bar that presents health ~~~ */
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.CYAN);
        // drawRect draws at left, top, right, bottom
        canvas.drawRect(s_bar, paint);
        /* ~~~ DONE ~~~ */

        /* ~~~ Draw current health of the pet ~~~ */
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.MAGENTA);
        // drawRect draws at left, top, right, bottom
        // Fill in the health bar depends of the current health of the pet
        canvas.drawRect(s_bar.left, s_bar.top,
                s_bar.left + ((ThePet.getCurrentStamina() * bar_width) / ThePet.getLevelStamina()),
                s_bar.bottom, paint);
        /* ~~~ DONE ~~~ */

        /* ~~~ Draw text ~~~ */
        paint.setColor(Color.BLACK);
        paint.setTextSize(text_size);
        paint.getTextBounds(text, 0, text.length(), text_bound);
        // drawText draws at left, bottom
        // That is, it starts drawing at the bottom left corner of a rect
        canvas.drawText(text, text_box.left + (text_box.width()-text_bound.width())/2,
                text_box.bottom - (text_box.height()-text_bound.height())/2, paint);
        /* ~~~ DONE ~~~ */
    }

//    private void drawXPBar(Canvas canvas) {
//        paint.setColor(Color.BLACK);
//        paint.setStrokeWidth(3);
//        canvas.drawRect(xp_bar, paint);
//        paint.setStrokeWidth(0);
//        paint.setColor(Color.CYAN);
//        canvas.drawRect(xp_bar, paint);
//        paint.setColor(Color.YELLOW);
//        canvas.drawRect(xp_bar, paint );
//    }
}
