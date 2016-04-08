package com.example.xuan.tictactoe;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PetInfo extends Activity {
    TextView birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pet_info);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        birthday = (TextView) findViewById(R.id.pet_birth);
        birthday.setText(birthday.getText() + getCurrentDate());
    }


    private String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        return sdf.format(cal.getTime());
    }



}
