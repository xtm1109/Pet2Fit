package com.example.xuan.pet2fit;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

public class TrainingField extends Activity implements SensorEventListener{
    /**
     * Earth gravity is around 9.8 m/s^2 but user may not completely direct his/her hand vertical
     * during the exercise so we leave some room. Basically if the x-component of gravity, as
     * measured by the Gravity sensor, changes with a variation (delta) > GRAVITY_THRESHOLD,
     * we consider that a successful count.
     */
    private static final float GRAVITY_THRESHOLD = 7.0f;
    /** an up-down movement that takes more than this will not be registered as such **/
    private static final long TIME_THRESHOLD_NS = 2000000000; // in nanoseconds (= 2sec)

    private long mLastTime;
    private boolean mUp;
    private int mJumpCounter;
    private int strengthGained;
    private SensorManager mSensorManager;
    private Sensor mSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_field);

        mLastTime = 0;
        mUp = false;
        mJumpCounter = 0;
        strengthGained = 0;

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        detectJump(event.values[0], event.timestamp);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * A simple algorithm to detect a successful up-down movement of hand(s). The algorithm is
     * based on the assumption that when a person is wearing the watch, the x-component of gravity
     * as measured by the Gravity Sensor is +9.8 when the hand is downward and -9.8 when the hand
     * is upward (signs are reversed if the watch is worn on the right hand). Since the upward or
     * downward may not be completely accurate, we leave some room and instead of 9.8, we use
     * GRAVITY_THRESHOLD. We also consider the up <-> down movement successful if it takes less than
     * TIME_THRESHOLD_NS.
     */
    private void detectJump(float xValue, long timestamp) {
        if ((Math.abs(xValue) > GRAVITY_THRESHOLD)) {
            if(timestamp - mLastTime < TIME_THRESHOLD_NS && mUp != (xValue > 0)) {
                onJumpDetected(!mUp);
            }
            mUp = xValue > 0;
            mLastTime = timestamp;
        }
    }

    /**
     * Called on detection of a successful down -> up or up -> down movement of hand.
     */
    private void onJumpDetected(boolean up) {
        // we only count a pair of up and down as one successful movement
        if (up) {
            return;
        }
        mJumpCounter++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mJumpCounter == 3) { // 3 JJs for 1 Strength
                    strengthGained++;
                    TextView strength = (TextView) findViewById(R.id.strength_counter);
                    strength.setText(Integer.toString(strengthGained));
                    mJumpCounter = 0;
                }
                TextView counter = (TextView) findViewById(R.id.jumping_jack_counter);
                counter.setText(Integer.toString(mJumpCounter));

            }
        });

        /*
         * Each Strength will take 20 Stamina away
         * from the pet. Calculate whether the pet
         * has enough Stamina to perform more JJ here.
         */
        if ((strengthGained*20) >= ThePet.getCurrentStamina()-20) {
            // Not enough Stamina for more, update text to show this
            mSensorManager.unregisterListener(this); // Stop sensor
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView warning = (TextView) findViewById(R.id.jumping_jack_title);
                    warning.setText(ThePet.getName() + " is exhausted...\n" +
                            "Cannot do anything now...");
                    TextView counter = (TextView) findViewById(R.id.jumping_jack_counter);
                    counter.setText("-");
                }
            });

        }

    }

    public void updateStrength(View view) {
        ThePet.setCurrentStrength(ThePet.getCurrentStrength() + strengthGained);
        ThePet.setCurrentStamina(ThePet.getCurrentStamina() - strengthGained*20);
        Intent intent = new Intent(this, MainGame.class);
        startActivity(intent);
    }
}
