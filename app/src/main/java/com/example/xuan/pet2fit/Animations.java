package com.example.xuan.pet2fit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Xuan on 2/30/2016.
 */
public class Animations extends SurfaceView implements Runnable {
    Thread game_thread = null;

    // SurfaceHolder for Paint and Canvas in a thread
    SurfaceHolder the_holder;

    // Canvas and Paint objects
    Canvas canvas;
    Paint paint;
    Bitmap dragon;

    volatile boolean is_running;

    long fps; // tracks the game frame rate
    private long time_this_frame; // calculate the fps
    float x_position = 0;  // start position
    float y_position = 0;
    long frame_ticker = 0l;

    // New variables for spritesheet
    private int frame_count = 11;  // Total frame in the sprite sheet
    private int sprite_width = 200; // this is the size of each frame
    private int sprite_height = 150;
    private int sprite_sheet;
    private int background_color = Color.argb(255, 255, 255, 255);
    private int current_frame = 0; // Start at the first frame

    // A rectangle to define an area of the sprite sheet that represents 1 frame
    private Rect frame_to_draw;

    // A rect that defines an area of the screen on which to draw
    RectF where_to_draw;

    // Constructor methods
    public Animations(Context context) {
        super(context);
        init(context);
    }

    public Animations(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Animations(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        Bundle transporter = ((Activity)getContext()).getIntent().getExtras();

        if (transporter != null) {
            prefs.edit().putInt("pet_choice", transporter.getInt("pet_choice")).commit();
        }
        sprite_sheet = setSpritesheet(prefs.getInt("pet_choice", 1));

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;

        // Load dragon from .png file
        dragon = BitmapFactory.decodeResource(this.getResources(), sprite_sheet);
        Bitmap o_dragon = BitmapFactory.decodeResource(this.getResources(),
                sprite_sheet, opts);
        sprite_width = (sprite_width * dragon.getWidth() / o_dragon.getWidth());
        sprite_height = (sprite_height * dragon.getHeight() / o_dragon.getHeight());

        frame_to_draw = new Rect(0, 0, sprite_width, sprite_height);
        where_to_draw = new RectF(x_position, 0, x_position + sprite_width, sprite_height);

        // Initialize Paint object
        paint = new Paint();

        // Initialize ourHolder and paint objects
        the_holder = getHolder();
        the_holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                pause();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                resume();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
        });
    }

    @Override
    public void run() {
        while (is_running) {
            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            update();  // Update the frame
            draw(); // Draw the frame

            // Calculate the fps this frame to time animations.
            time_this_frame = System.currentTimeMillis() - startFrameTime;
            if (time_this_frame >= 1) {
                fps = 1500 / time_this_frame;
            }
        }
    }

    // Everything that needs to be updated goes in here
    // In later projects we will have dozens (arrays) of objects.
    // We will also do other things like collision detection.
    public void update() {
        if (!is_running)
            return;
        long game_time = System.currentTimeMillis();

        if (game_time > (frame_ticker + fps)) {
            frame_ticker = game_time;

            current_frame++;
            if (current_frame >= frame_count) {
                current_frame = 0;
            }
        }

        frame_to_draw.left = current_frame * sprite_width;
        frame_to_draw.right = frame_to_draw.left + sprite_width;
    }

    // Draw the newly updated scene
    public void draw() {
        if (!is_running) {
            return;
        }
        // Make sure our drawing surface is valid or we crash
        if (the_holder.getSurface().isValid()) {
            x_position = (float) ((this.getWidth()-frame_to_draw.width())/2.0);
            y_position = (float) ((this.getHeight()-frame_to_draw.height())/2.0);

            where_to_draw.set((int) x_position,
                    (int) y_position,
                    (int) x_position + sprite_width,
                    (int) y_position + sprite_height);

            canvas = the_holder.lockCanvas(); // Lock the canvas ready to draw

            if (canvas != null) {
                paint.setColor(Color.argb(255, 249, 129, 0)); // Choose the brush color for drawing
                canvas.drawColor(background_color); // Draw the background color
                canvas.drawBitmap(dragon,
                        frame_to_draw,
                        where_to_draw, paint);


                the_holder.unlockCanvasAndPost(canvas); // Draw everything to the screen
            }
        }
    }

    // If activity is paused/stopped shutdown our thread.
    public void pause() {
        is_running = false;
        try {
            game_thread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
        game_thread = null;

    }

    // If activity is started then start our thread.
    public void resume() {
        is_running = true;
        game_thread = new Thread(this);
        game_thread.start();
    }

    private int setSpritesheet(int pet_choice) {
        switch (pet_choice) {
            case 2:
                return R.drawable.bkd_spritesheet;
            case 3:
                return R.drawable.gd_spritesheet;
            case 4:
                return R.drawable.grd_spritesheet;
            case 5:
                return R.drawable.rd_spritesheet;
            default:
                return R.drawable.bd_spritesheet;
        }

    }
}
